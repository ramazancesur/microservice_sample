from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import uuid

app = FastAPI(title="Order Service", version="1.0.0")

# In-memory store (replace with a real DB in production)
orders_db: dict = {}


class OrderItem(BaseModel):
    product_id: str
    quantity: int


class OrderCreate(BaseModel):
    user_id: str
    items: List[OrderItem]


class Order(BaseModel):
    id: str
    user_id: str
    items: List[OrderItem]
    status: str = "pending"


@app.get("/health")
def health_check():
    return {"status": "ok", "service": "order-service"}


@app.get("/orders", response_model=List[Order])
def list_orders():
    return list(orders_db.values())


@app.post("/orders", response_model=Order, status_code=201)
def create_order(payload: OrderCreate):
    order_id = str(uuid.uuid4())
    order = Order(
        id=order_id,
        user_id=payload.user_id,
        items=payload.items,
        status="pending",
    )
    orders_db[order_id] = order
    return order


@app.get("/orders/{order_id}", response_model=Order)
def get_order(order_id: str):
    order = orders_db.get(order_id)
    if not order:
        raise HTTPException(status_code=404, detail="Order not found")
    return order


@app.patch("/orders/{order_id}/status", response_model=Order)
def update_order_status(order_id: str, status: str):
    order = orders_db.get(order_id)
    if not order:
        raise HTTPException(status_code=404, detail="Order not found")
    valid_statuses = {"pending", "confirmed", "shipped", "delivered", "cancelled"}
    if status not in valid_statuses:
        raise HTTPException(status_code=400, detail=f"Invalid status. Must be one of: {valid_statuses}")
    order.status = status
    orders_db[order_id] = order
    return order
