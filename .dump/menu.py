import requests

url_ingredient = 'http://localhost:8080/ingredients'
map_ingredient = {}
url_product = 'http://localhost:8080/products'
map_product = {}


def create_ingredient_and_save_id(ingredient):
    response = requests.post(url_ingredient, json=ingredient)
    if response.status_code == 200:
        data = response.json()
        map_ingredient[ingredient['name']] = data.get('ingredientId')
        print(f'{ingredient["name"]}: {data.get("ingredientId")}')
    else:
        print(f'Erro na requisição: {response.status_code}')
        return None


def create_product_and_save_id(product):
    response = requests.post(url_product, json=product)
    if response.status_code == 200:
        data = response.json()
        return data.get("productId")
    else:
        print(f"Erro na requisição: {response.status_code}")
        return None


def create_all_ingredients():
    hamburguer = {"name": "Hamburguer", "category": "burger", "price": 3.99}
    queijo = {"name": "Queijo", "category": "burger", "price": 1.99}
    alface = {"name": "Alface", "category": "burger", "price": 0.99}
    sal = {"name": "Sal", "category": "accompaniment", "price": 0.1}
    sache = {"name": "Sache", "category": "accompaniment", "price": 0.1}
    gelo = {"name": "Gelo", "category": "drink", "price": 0.1}
    limao = {"name": "Limao", "category": "drink", "price": 0.1}
    chocolate = {"name": "Chocolate", "category": "dessert", "price": 2.99}
    ninho = {"name": "Ninho", "category": "dessert", "price": 1.99}

    create_ingredient_and_save_id(hamburguer)
    create_ingredient_and_save_id(queijo)
    create_ingredient_and_save_id(alface)
    create_ingredient_and_save_id(sal)
    create_ingredient_and_save_id(sache)
    create_ingredient_and_save_id(gelo)
    create_ingredient_and_save_id(limao)
    create_ingredient_and_save_id(chocolate)
    create_ingredient_and_save_id(ninho)


create_all_ingredients()
