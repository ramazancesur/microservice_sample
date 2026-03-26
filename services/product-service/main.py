from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional
import uuid

app = FastAPI(title="Product Service", version="1.0.0")

# In-memory store (replace with a real DB in production)
products_db: dict = {}


class ProductCreate(BaseModel):
    name: str
    description: Optional[str] = None
    price: float
    stock: int = 0


class Product(BaseModel):
    id: str
    name: str
    description: Optional[str] = None
    price: float
    stock: int


@app.get("/health")
def health_check():
    return {"status": "ok", "service": "product-service"}


@app.get("/products", response_model=List[Product])
def list_products():
    return list(products_db.values())


@app.post("/products", response_model=Product, status_code=201)
def create_product(payload: ProductCreate):
    product_id = str(uuid.uuid4())
    product = Product(
        id=product_id,
        name=payload.name,
        description=payload.description,
        price=payload.price,
        stock=payload.stock,
    )
    products_db[product_id] = product
    return product


@app.get("/products/{product_id}", response_model=Product)
def get_product(product_id: str):
    product = products_db.get(product_id)
    if not product:
        raise HTTPException(status_code=404, detail="Product not found")
    return product


@app.delete("/products/{product_id}", status_code=204)
def delete_product(product_id: str):
    if product_id not in products_db:
        raise HTTPException(status_code=404, detail="Product not found")
    del products_db[product_id]
