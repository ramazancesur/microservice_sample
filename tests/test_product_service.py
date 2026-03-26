import pytest
from fastapi.testclient import TestClient
from conftest import load_service

product_main = load_service("product-service", "product_main")


@pytest.fixture(autouse=True)
def reset_db():
    product_main.products_db.clear()
    yield
    product_main.products_db.clear()


@pytest.fixture
def client():
    return TestClient(product_main.app)


def test_health(client):
    resp = client.get("/health")
    assert resp.status_code == 200
    assert resp.json()["service"] == "product-service"


def test_create_and_get_product(client):
    payload = {"name": "Widget", "description": "A fine widget", "price": 9.99, "stock": 100}
    resp = client.post("/products", json=payload)
    assert resp.status_code == 201
    data = resp.json()
    assert data["name"] == "Widget"
    assert data["price"] == 9.99
    product_id = data["id"]

    resp = client.get(f"/products/{product_id}")
    assert resp.status_code == 200
    assert resp.json()["id"] == product_id


def test_list_products(client):
    client.post("/products", json={"name": "A", "price": 1.0, "stock": 10})
    client.post("/products", json={"name": "B", "price": 2.0, "stock": 20})
    resp = client.get("/products")
    assert resp.status_code == 200
    assert len(resp.json()) == 2


def test_get_nonexistent_product(client):
    resp = client.get("/products/no-such-id")
    assert resp.status_code == 404


def test_delete_product(client):
    resp = client.post("/products", json={"name": "X", "price": 5.0, "stock": 1})
    product_id = resp.json()["id"]
    resp = client.delete(f"/products/{product_id}")
    assert resp.status_code == 204
    assert client.get(f"/products/{product_id}").status_code == 404

