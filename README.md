# microservice_sample

Örnek microservis projesi / Sample Microservice Project

---

## 🇹🇷 Türkçe

### Proje Hakkında

Bu proje, temel bir **mikroservis mimarisi** örneğini göstermektedir. Gerçek bir e-ticaret senaryosunu taklit ederek üç bağımsız servisi ve bir API Gateway'i bir araya getirir.

### Mimari

```
                    ┌─────────────────────────────────┐
    İstemci ──────▶ │        API Gateway (Nginx)       │ :80
                    └────────────┬───────────┬─────────┘
                                 │           │
               ┌─────────────────┼───────────┼──────────────┐
               │                 │           │              │
               ▼                 ▼           ▼              │
     ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
     │ User Service │  │Product Service│  │Order Service │  │
     │    :8000     │  │    :8001      │  │    :8002     │  │
     └──────────────┘  └──────────────┘  └──────────────┘  │
                                                             │
                              microservice-net (Docker ağı) ─┘
```

### Servisler

| Servis            | Port (iç) | Açıklama                  |
| ----------------- | --------- | ------------------------- |
| **API Gateway**   | 80        | Yönlendirme (Nginx)       |
| **User Service**  | 8000      | Kullanıcı yönetimi        |
| **Product Service** | 8001    | Ürün kataloğu             |
| **Order Service** | 8002      | Sipariş işlemleri         |

### Gereksinimler

- [Docker](https://www.docker.com/) (>= 20.x)
- [Docker Compose](https://docs.docker.com/compose/) (>= 2.x)

### Kurulum ve Çalıştırma

```bash
# 1. Repo'yu klonlayın
git clone https://github.com/ramazancesur/microservice_sample.git
cd microservice_sample

# 2. (İsteğe bağlı) Ortam değişkenlerini ayarlayın
cp .env.example .env

# 3. Tüm servisleri başlatın
docker compose up --build

# 4. Servislerin çalışıp çalışmadığını kontrol edin
curl http://localhost/health
```

### API Endpoints

#### Kullanıcılar
| Method | URL                    | Açıklama              |
| ------ | ---------------------- | --------------------- |
| GET    | /api/users             | Tüm kullanıcıları listele |
| POST   | /api/users             | Yeni kullanıcı oluştur |
| GET    | /api/users/{id}        | Kullanıcı getir       |
| DELETE | /api/users/{id}        | Kullanıcı sil         |

#### Ürünler
| Method | URL                    | Açıklama              |
| ------ | ---------------------- | --------------------- |
| GET    | /api/products          | Tüm ürünleri listele  |
| POST   | /api/products          | Yeni ürün oluştur     |
| GET    | /api/products/{id}     | Ürün getir            |
| DELETE | /api/products/{id}     | Ürün sil              |

#### Siparişler
| Method | URL                             | Açıklama              |
| ------ | ------------------------------- | --------------------- |
| GET    | /api/orders                     | Tüm siparişleri listele |
| POST   | /api/orders                     | Yeni sipariş oluştur  |
| GET    | /api/orders/{id}                | Sipariş getir         |
| PATCH  | /api/orders/{id}/status?status= | Sipariş durumunu güncelle |

### Testleri Çalıştırma

```bash
pip install fastapi uvicorn pydantic httpx pytest
python -m pytest tests/ -v
```

### Servisleri Durdurma

```bash
docker compose down
```

---

## 🇬🇧 English

### About

This project demonstrates a minimal **microservice architecture** using Python/FastAPI services behind an Nginx API Gateway, all orchestrated with Docker Compose.

### Quick Start

```bash
git clone https://github.com/ramazancesur/microservice_sample.git
cd microservice_sample
docker compose up --build
```

Visit `http://localhost/health` to confirm the gateway is running.

Each service also exposes an interactive Swagger UI when run standalone:
- User Service: `http://localhost:8000/docs`
- Product Service: `http://localhost:8001/docs`
- Order Service: `http://localhost:8002/docs`

### Project Structure

```
microservice_sample/
├── api-gateway/
│   ├── Dockerfile
│   └── nginx/
│       └── default.conf       # Nginx routing config
├── services/
│   ├── user-service/
│   │   ├── Dockerfile
│   │   ├── main.py
│   │   └── requirements.txt
│   ├── product-service/
│   │   ├── Dockerfile
│   │   ├── main.py
│   │   └── requirements.txt
│   └── order-service/
│       ├── Dockerfile
│       ├── main.py
│       └── requirements.txt
├── tests/
│   ├── conftest.py
│   ├── test_user_service.py
│   ├── test_product_service.py
│   └── test_order_service.py
├── docker-compose.yml
├── .env.example
└── README.md
```

### Running Tests

```bash
pip install fastapi uvicorn pydantic httpx pytest
python -m pytest tests/ -v
```
