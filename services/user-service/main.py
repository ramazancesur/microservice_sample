from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional
import uuid

app = FastAPI(title="User Service", version="1.0.0")

# In-memory store (replace with a real DB in production)
users_db: dict = {}


class UserCreate(BaseModel):
    name: str
    email: str


class User(BaseModel):
    id: str
    name: str
    email: str


@app.get("/health")
def health_check():
    return {"status": "ok", "service": "user-service"}


@app.get("/users", response_model=List[User])
def list_users():
    return list(users_db.values())


@app.post("/users", response_model=User, status_code=201)
def create_user(payload: UserCreate):
    user_id = str(uuid.uuid4())
    user = User(id=user_id, name=payload.name, email=payload.email)
    users_db[user_id] = user
    return user


@app.get("/users/{user_id}", response_model=User)
def get_user(user_id: str):
    user = users_db.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return user


@app.delete("/users/{user_id}", status_code=204)
def delete_user(user_id: str):
    if user_id not in users_db:
        raise HTTPException(status_code=404, detail="User not found")
    del users_db[user_id]
