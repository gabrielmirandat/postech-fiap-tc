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
        map_product[product['name']] = data.get('productId')
        print(f'{product["name"]}: {data.get("productId")}')
    else:
        print(f"Erro na requisição: {response.status_code}")
        return None


def create_all_ingredients():
    hamburguer = {"name": "Hamburguer", "category": "burger",
                  "price": 3.99, "weight": 200, "isExtra": True}
    queijo_cheddar = {"name": "QueijoCheddar", "category": "burger",
                      "price": 1.99, "weight": 50, "isExtra": True}
    alface = {"name": "Alface", "category": "burger",
              "price": 0.99, "weight": 50, "isExtra": True}
    sal = {"name": "Sal", "category": "accompaniment",
           "price": 0.1, "weight": 10, "isExtra": True}
    sache = {"name": "Sache", "category": "accompaniment",
             "price": 0.1, "weight": 10, "isExtra": True}
    gelo = {"name": "Gelo", "category": "drink",
            "price": 0.1, "weight": 10, "isExtra": True}
    limao = {"name": "Limao", "category": "drink",
             "price": 0.1, "weight": 15, "isExtra": True}
    chocolate = {"name": "Chocolate", "category": "dessert",
                 "price": 2.99, "weight": 100, "isExtra": True}
    ninho = {"name": "Ninho", "category": "dessert",
             "price": 1.99, "weight": 30, "isExtra": True}
    batata = {"name": "Batata", "category": "accompaniment",
              "price": 1.99, "weight": 30, "isExtra": False}
    queijo_mussarela = {"name": "QueijoMussarela", "category": "accompaniment",
                        "price": 2.99, "weight": 50, "isExtra": False}
    guarana = {"name": "Guarana", "category": "drink",
               "price": 4.00, "weight": 250, "isExtra": False}
    sorvete = {"name": "Creme", "category": "dessert",
               "price": 3.00, "weight": 70, "isExtra": False}

    create_ingredient_and_save_id(hamburguer)
    create_ingredient_and_save_id(queijo_cheddar)
    create_ingredient_and_save_id(alface)
    create_ingredient_and_save_id(sal)
    create_ingredient_and_save_id(sache)
    create_ingredient_and_save_id(gelo)
    create_ingredient_and_save_id(limao)
    create_ingredient_and_save_id(chocolate)
    create_ingredient_and_save_id(ninho)
    create_ingredient_and_save_id(batata)
    create_ingredient_and_save_id(queijo_mussarela)
    create_ingredient_and_save_id(guarana)
    create_ingredient_and_save_id(sorvete)


def create_all_products():
    x_burgao = {"name": "X-Burgao", "description": "Carro chefe da casa",
                "category": "burger", "price": 10.90,
                "image": "any.png",
                "ingredients": [{
                    "ingredientId": map_ingredient["Hamburguer"],
                    "quantity": 2
                }, {
                    "ingredientId": map_ingredient["QueijoCheddar"],
                    "quantity": 1
                }, {
                    "ingredientId": map_ingredient["Alface"],
                    "quantity": 1
                }]}

    x_salada = {"name": "X-Salada", "description": "Para quem gosta de salada",
                "category": "burger", "price": 8.90,
                "image": "any.png",
                "ingredients": [{
                    "ingredientId": map_ingredient["Hamburguer"],
                    "quantity": 1
                }, {
                    "ingredientId": map_ingredient["QueijoCheddar"],
                    "quantity": 1
                }, {
                    "ingredientId": map_ingredient["Alface"],
                    "quantity": 3
                }]}

    batata_frita = {"name": "Batata Frita", "description": "Aquela french fries",
                    "category": "accompaniment", "price": 6.90,
                    "image": "any.png",
                    "ingredients": [{
                        "ingredientId": map_ingredient["Batata"],
                        "quantity": 1
                    }]}

    bola_queijo = {"name": "Bola de Queijo", "description": "Bolinhas crocantes de queijo",
                   "category": "accompaniment", "price": 5.90,
                   "image": "any.png",
                   "ingredients": [{
                       "ingredientId": map_ingredient["QueijoMussarela"],
                       "quantity": 2
                   }]}

    refrigerante = {"name": "Guaranazin", "description": "Guaranazin do amor",
                    "category": "drink", "price": 4.90,
                    "image": "any.png",
                    "ingredients": [{
                        "ingredientId": map_ingredient["Guarana"],
                        "quantity": 1
                    }]}

    sorvete = {"name": "SorveteCreme", "description": "Sorvete pra matar a vontade",
               "category": "dessert", "price": 4.90,
               "image": "any.png",
               "ingredients": [{
                   "ingredientId": map_ingredient["Creme"],
                   "quantity": 1
               }]}

    create_product_and_save_id(x_burgao)
    create_product_and_save_id(x_salada)
    create_product_and_save_id(batata_frita)
    create_product_and_save_id(bola_queijo)
    create_product_and_save_id(refrigerante)
    create_product_and_save_id(sorvete)


create_all_ingredients()
create_all_products()
