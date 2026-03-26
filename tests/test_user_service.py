import pytest
from fastapi.testclient import TestClient
from conftest import load_service

user_main = load_service("user-service", "user_main")


@pytest.fixture(autouse=True)
def reset_db():
    """Clear the in-memory DB before each test."""
    user_main.users_db.clear()
    yield
    user_main.users_db.clear()


@pytest.fixture
def client():
    return TestClient(user_main.app)


def test_health(client):
    resp = client.get("/health")
    assert resp.status_code == 200
    assert resp.json()["service"] == "user-service"


def test_create_and_get_user(client):
    payload = {"name": "Alice", "email": "alice@example.com"}
    resp = client.post("/users", json=payload)
    assert resp.status_code == 201
    data = resp.json()
    assert data["name"] == "Alice"
    assert data["email"] == "alice@example.com"
    user_id = data["id"]

    resp = client.get(f"/users/{user_id}")
    assert resp.status_code == 200
    assert resp.json()["id"] == user_id


def test_list_users(client):
    client.post("/users", json={"name": "Bob", "email": "bob@example.com"})
    client.post("/users", json={"name": "Carol", "email": "carol@example.com"})
    resp = client.get("/users")
    assert resp.status_code == 200
    assert len(resp.json()) == 2


def test_get_nonexistent_user(client):
    resp = client.get("/users/nonexistent-id")
    assert resp.status_code == 404


def test_delete_user(client):
    resp = client.post("/users", json={"name": "Dave", "email": "dave@example.com"})
    user_id = resp.json()["id"]
    resp = client.delete(f"/users/{user_id}")
    assert resp.status_code == 204
    resp = client.get(f"/users/{user_id}")
    assert resp.status_code == 404

