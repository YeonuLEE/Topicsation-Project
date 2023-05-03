import requests
from bs4 import BeautifulSoup
import json
import mysql.connector
import datetime
import sys
import time

cnx = mysql.connector.connect(
    host="localhost",
    port="3306",
    user="webuser",
    password="webuser",
    database="webdb"
)

cursor = cnx.cursor()

def get_article_content(article_url):
    article_response = requests.get(article_url)
    article_soup = BeautifulSoup(article_response.text, 'html.parser')
    article_paragraphs = article_soup.find_all('div', {'data-component': 'text-block'})
    return ' '.join([paragraph.get_text(strip=True) for paragraph in article_paragraphs])

# 각 카테고리의 URL
def add_today_news(data):
    # Home 카테고리 뉴스를 가져옵니다.
    home_news = crawl_home_news()
    data['today'] = home_news['today']

    return data

def crawl_bbc_news():
    today = datetime.datetime.today().strftime('%Y-%m-%d')
    urls = [
        'https://www.bbc.com/news/business',
        'https://www.bbc.com/news/technology',
        'https://www.bbc.com/news/science_and_environment',
        'https://www.bbc.com/news/entertainment_and_arts',
        'https://www.bbc.com/news/health'
    ]
    data = {
        'business': {},
        'tech': {},
        'science': {},
        'entertainment': {},
        'health': {},
        'today': {}
    }

    # 각 카테고리의 뉴스를 가져옵니다.
    for url_index, url in enumerate(urls):
        sys.stdout.write(f"\r크롤링 중: {url} ({url_index + 1}/{len(urls)})")
        sys.stdout.flush()
        response = requests.get(url)
        soup = BeautifulSoup(response.text, 'html.parser')
        articles = soup.find_all('div', {'class': 'gs-c-promo'})

        found_article = False

        for article in articles:
            title_element = article.find('h3', {'class': 'gs-c-promo-heading__title'})
            link_element = article.find('a', {'class': 'gs-c-promo-heading'})

            if title_element is None or link_element is None:
                continue

            title = title_element.get_text(strip=True)
            article_url = link_element['href']

            if not article_url.startswith('http'):
                article_url = f"https://www.bbc.com{article_url}"

            content = get_article_content(article_url)
            if not content:
                continue

            category_key = ""
            if "business" in url:
                category_key = "business"
            elif "technology" in url:
                category_key = "tech"
            elif "science" in url:
                category_key = "science"
            elif "entertainment" in url:
                category_key = "entertainment"
            elif "health" in url:
                category_key = "health"

            if category_key:
                data[category_key] = {
                    'title': title,
                    'content': content,
                    'url': article_url
                }

                found_article = True

    # Home 카테고리 뉴스를 추가합니다.
    sys.stdout.write("\n")
    data = add_today_news(data)
    print()
    return data

def crawl_home_news():
    url = 'https://www.bbc.com/news'
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')

    # Home 카테고리 뉴스를 가져옵니다.
    articles = soup.find_all('div', {'class': 'gs-c-promo'})


    today_news = None

    for article in articles:
        title_element = article.find('h3', {'class': 'gs-c-promo-heading__title'})
        link_element = article.find('a', {'class': 'gs-c-promo-heading'})

        if title_element is None or link_element is None:
            continue

        title = title_element.get_text(strip=True)
        article_url = link_element['href']

        if not article_url.startswith('http'):
            article_url = f"https://www.bbc.com{article_url}"

        content = get_article_content(article_url)
        if not content:
            continue

        today_news = {
            'title': title,
            'content': content,
            'url': article_url
        }
        break

    data = {
        'today': today_news
    }

    return data

def save_news_to_database(data):
    for category_key, category_value in data.items():
        if category_key == 'business':
            news_id = 1
        elif category_key == 'tech':
            news_id = 2
        elif category_key == 'science':
            news_id = 3
        elif category_key == 'entertainment':
            news_id = 4
        elif category_key == 'health':
            news_id = 5
        elif category_key == 'today':
            news_id = 6
        else:
            continue

        newsjson = json.dumps(category_value, ensure_ascii=False)
        update_query = f"UPDATE news_suggestion SET newsjson = %s WHERE news_id = %s"
        cursor.execute(update_query, (newsjson, news_id))

    cnx.commit()
    print("Database Update Success")

def main():
    while True:
        start_time = time.time()
        print(f"{datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')} 작업 실행 중...")
        news_data = crawl_bbc_news()
        save_news_to_database(news_data)
        end_time = time.time()
        elapsed_time = end_time - start_time
        print(f"작업 시작 시간: {time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(start_time))}")
        print(f"작업 종료 시간: {time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(end_time))}")
        print(f"작업 소요 시간: {elapsed_time:.2f}초")
        print("다음 업데이트까지 대기 중...")
        sys.stdout.flush()
        time.sleep(10800)  # 작업 완료 후 대기 시간을 추가합니다.

if __name__ == "__main__":
    main()