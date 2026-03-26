import pytest
from fastapi.testclient import TestClient
from conftest import load_service

order_main = load_service("order-service", "order_main")


@pytest.fixture(autouse=True)
def reset_db():
    order_main.orders_db.clear()
    yield
    order_main.orders_db.clear()


@pytest.fixture
def client():
    return TestClient(order_main.app)


def test_health(client):
    resp = client.get("/health")
    assert resp.status_code == 200
    assert resp.json()["service"] == "order-service"


def test_create_and_get_order(client):
    payload = {
        "user_id": "user-123",
        "items": [{"product_id": "prod-1", "quantity": 2}],
    }
    resp = client.post("/orders", json=payload)
    assert resp.status_code == 201
    data = resp.json()
    assert data["user_id"] == "user-123"
    assert data["status"] == "pending"
    order_id = data["id"]

    resp = client.get(f"/orders/{order_id}")
    assert resp.status_code == 200
    assert resp.json()["id"] == order_id


def test_list_orders(client):
    payload = {"user_id": "u1", "items": [{"product_id": "p1", "quantity": 1}]}
    client.post("/orders", json=payload)
    client.post("/orders", json=payload)
    resp = client.get("/orders")
    assert resp.status_code == 200
    assert len(resp.json()) == 2


def test_update_order_status(client):
    payload = {"user_id": "u2", "items": [{"product_id": "p2", "quantity": 3}]}
    resp = client.post("/orders", json=payload)
    order_id = resp.json()["id"]

    resp = client.patch(f"/orders/{order_id}/status", params={"status": "confirmed"})
    assert resp.status_code == 200
    assert resp.json()["status"] == "confirmed"


def test_invalid_status(client):
    payload = {"user_id": "u3", "items": [{"product_id": "p3", "quantity": 1}]}
    resp = client.post("/orders", json=payload)
    order_id = resp.json()["id"]

    resp = client.patch(f"/orders/{order_id}/status", params={"status": "flying"})
    assert resp.status_code == 400


def test_get_nonexistent_order(client):
    resp = client.get("/orders/no-such-id")
    assert resp.status_code == 404

