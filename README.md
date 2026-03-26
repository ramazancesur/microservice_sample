# Banking Microservice Platform

Mikroservis mimarisi ile geliştirilmiş bankacılık uygulaması.

**Backend:** Java 21, Spring Boot 3.3.4, Maven Multi-Module, Clean Architecture  
**Frontend:** React 18, TypeScript, Vite 5, Module Federation, Material UI  
**Database:** PostgreSQL 16 (servis başına ayrı)  
**Logging:** ELK Stack (Elasticsearch, Logstash, Kibana) + Filebeat

---

## Proje Yapısı

```
microservice/
├── microbackend/                # Backend (Maven Multi-Module)
│   ├── api-gateway/             # Spring Cloud Gateway (port 8080)
│   ├── customer-service/        # Müşteri yönetimi (port 8081)
│   ├── account-service/         # Hesap yönetimi (port 8082)
│   ├── ledger-service/          # Muhasebe / çift taraflı kayıt (port 8083)
│   ├── payment-service/         # Ödeme işlemleri (port 8084)
│   └── pom.xml                  # Parent POM
├── microui/                     # Frontend (Module Federation Monorepo)
│   ├── shell/                   # Ana uygulama - host (port 3000)
│   ├── remote-customers/        # Müşteri microfrontend (port 3001)
│   ├── remote-accounts/         # Hesap microfrontend (port 3002)
│   ├── remote-payments/         # Ödeme microfrontend (port 3003)
│   ├── remote-ledger/           # Muhasebe microfrontend (port 3004)
│   └── package.json             # Workspace root
├── elk/                         # ELK Stack konfigürasyonları
│   ├── filebeat/filebeat.yml
│   └── logstash/
├── docker-compose.yml
└── README.md
```

---

## Hızlı Başlangıç

### 1. Backend + Veritabanları + ELK (Docker)

```bash
docker compose up -d --build
```

Servislerin tamamen ayağa kalkması ~60-90 saniye sürebilir. Kontrol:

```bash
docker compose ps
curl http://localhost:8080/actuator/health
```

### 2. Frontend

```bash
cd microui
npm install
npm run start
```

Bu komut sırasıyla:
1. Remote'ları build edip preview sunucularını başlatır (3001-3004)
2. Shell, remote'lar hazır olunca build olup başlar (3000)
3. Tarayıcıda `http://localhost:3000` adresini aç

---

## Port Tablosu

| Servis | Port | Açıklama |
|---|---|---|
| API Gateway | 8080 | Tüm backend istekleri buradan geçer |
| Customer Service | 8081 | Müşteri CRUD |
| Account Service | 8082 | Hesap açma/listeleme |
| Ledger Service | 8083 | Bakiye & muhasebe hareketleri |
| Payment Service | 8084 | Ödeme başlatma (idempotent) |
| PostgreSQL (customer) | 5432 | customer_db |
| PostgreSQL (account) | 5433 | account_db |
| PostgreSQL (ledger) | 5434 | ledger_db |
| PostgreSQL (payment) | 5435 | payment_db |
| Elasticsearch | 9200 | Log depolama |
| Logstash | 5044 | Log pipeline |
| Kibana | 5601 | Log görselleştirme |
| Shell (Frontend) | 3000 | Ana UI |
| Remote Customers | 3001 | Müşteri microfrontend |
| Remote Accounts | 3002 | Hesap microfrontend |
| Remote Payments | 3003 | Ödeme microfrontend |
| Remote Ledger | 3004 | Muhasebe microfrontend |

---

## API Endpoints (Gateway üzerinden)

### Customers
- `GET  /api/v1/customers?q=keyword&page=0&size=20` — Müşteri listele/ara
- `GET  /api/v1/customers/{id}` — Müşteri detay
- `POST /api/v1/customers` — Yeni müşteri
- `PUT  /api/v1/customers/{id}` — Müşteri güncelle

### Accounts
- `GET  /api/v1/accounts?q=accountNumber&page=0&size=20` — Hesap listele/ara
- `GET  /api/v1/accounts/{id}` — Hesap detay
- `GET  /api/v1/accounts/customer/{customerId}` — Müşterinin hesapları
- `POST /api/v1/accounts` — Hesap aç

### Ledger
- `GET  /api/v1/ledger/balance/{accountId}?currency=TRY` — Hesap bakiye
- `GET  /api/v1/ledger/movements/{accountId}?from=...&to=...` — Hareket listesi
- `POST /api/v1/ledger/journal` — Muhasebe kaydı

### Payments
- `GET  /api/v1/payments?page=0&size=20` — Ödeme listesi
- `GET  /api/v1/payments/{id}` — Ödeme detay
- `POST /api/v1/payments` — Ödeme başlat (idempotent)

### Swagger UI
Her servis kendi Swagger UI'ına sahiptir:
- http://localhost:8081/swagger-ui.html (Customer)
- http://localhost:8082/swagger-ui.html (Account)
- http://localhost:8083/swagger-ui.html (Ledger)
- http://localhost:8084/swagger-ui.html (Payment)

---

## Dikkat Edilmesi Gereken Noktalar

### Backend

1. **Clean Architecture katmanlarına uy:** Her servis `domain`, `application`, `infrastructure`, `api` katmanlarına sahip. Domain katmanı hiçbir framework'e bağımlı olmamalı.

2. **Domain nesneleri immutable olmalı:** `Money` gibi Value Object'ler `record` ile tanımlı. Entity'ler `reconstitute()` factory method ile oluşturulur.

3. **Flyway migration dosyaları değiştirme:** `V1__create_*.sql` dosyaları bir kez çalıştıktan sonra **kesinlikle** değiştirilmemeli. Yeni değişiklikler `V2__`, `V3__` şeklinde yeni dosyalarla yapılmalı.

4. **CHAR(3) değil VARCHAR(3) kullan:** PostgreSQL'de `CHAR` tipi `bpchar` olarak saklanır, Hibernate `VARCHAR` bekler. Currency gibi sabit uzunluklu alanlar için bile `VARCHAR(3)` kullan.

5. **Idempotency key:** Payment servisi idempotent çalışır. Aynı `idempotencyKey` ile tekrar istek atılırsa duplicate ödeme oluşmaz.

6. **Backend servisleri Docker ile çalıştır:** Sistem JDK'sı (Java 25) ile Lombok uyumlu değil. Docker image'lar JDK 21 kullanır. Local development için JDK 21 kurulmalı.

7. **Docker rebuild sonrası gateway restart:** Backend servislerini `docker compose build --no-cache` ile rebuild ettikten sonra gateway'i de restart et, yoksa eski IP'lere bağlanmaya çalışır:
   ```bash
   docker compose build --no-cache account-service customer-service
   docker compose up -d account-service customer-service
   docker compose restart api-gateway
   ```

8. **Actuator endpoint'leri:** Her servis `/actuator/health` endpoint'ine sahip. Gateway health check için bunu kullanır.

9. **GlobalExceptionHandler:** Her serviste `@RestControllerAdvice` ile merkezi hata yönetimi var. Yeni exception tipleri eklerken burayı güncelle.

10. **Loglar JSON formatında:** Logstash Logback Encoder ile yapılandırılmış. `log.info("mesaj")` şeklinde loglama yap, loglar otomatik olarak ELK'ya iletilir.

### Frontend — Module Federation Mimarisi (Detaylı)

#### Mimari Genel Bakış

Bu projede **Vite Module Federation** (`@originjs/vite-plugin-federation`) kullanılıyor. Mimari şöyle çalışır:

```
┌─────────────────────────────────────────────────────┐
│  Shell (Host) — localhost:3000                      │
│  ┌─────────────┐ ┌─────────────┐ ┌──────────────┐  │
│  │ ThemeProvider│ │ AppLayout   │ │ BrowserRouter│  │
│  └──────┬──────┘ └──────┬──────┘ └──────┬───────┘  │
│         │               │               │           │
│   ┌─────┴───────────────┴───────────────┴─────┐     │
│   │     <Suspense> + <Routes>                 │     │
│   │       /customers/* → remoteCustomers      │     │
│   │       /accounts/*  → remoteAccounts       │     │
│   │       /payments/*  → remotePayments       │     │
│   │       /ledger/*    → remoteLedger         │     │
│   └───────────────────────────────────────────┘     │
└──────────┬──────────┬──────────┬──────────┬─────────┘
           │          │          │          │
    ┌──────┴──┐ ┌─────┴───┐ ┌───┴─────┐ ┌─┴────────┐
    │ Remote  │ │ Remote  │ │ Remote  │ │ Remote   │
    │Customers│ │Accounts │ │Payments │ │ Ledger   │
    │ :3001   │ │ :3002   │ │ :3003   │ │ :3004    │
    └─────────┘ └─────────┘ └─────────┘ └──────────┘
```

**Shell (Host):** Tüm uygulamanın iskeleti. `ThemeProvider`, `BrowserRouter`, `AppLayout` (sidebar + topbar) burada tanımlı. Remote'ları `lazy()` ile dinamik olarak yükler.

**Remote'lar:** Her biri bağımsız bir Vite projesi. Kendi `exposes` konfigürasyonuyla bir root component dışa açar (ör. `CustomersApp`). Shell bu component'i `remoteEntry.js` üzerinden runtime'da yükler.

#### Dosya Yapısı Kuralı

Her remote aynı yapıya sahip olmalı:

```
remote-customers/
├── src/
│   ├── CustomersApp.tsx        ← Federation'ın expose ettiği root component
│   ├── api/
│   │   └── customerApi.ts      ← Backend API çağrıları
│   ├── components/
│   │   ├── CustomerTable.tsx    ← DataGrid tablosu
│   │   ├── CustomerForm.tsx     ← Form bileşeni
│   │   ├── FormDialog.tsx       ← Genel dialog wrapper
│   │   └── PageHeader.tsx       ← Sayfa başlığı + action butonları
│   ├── pages/
│   │   └── CustomerListPage.tsx ← Sayfa seviyesi bileşen (state yönetimi)
│   └── types/
│       └── customer.ts          ← TypeScript tipleri / interface'ler
├── vite.config.ts               ← Federation konfigürasyonu
├── tsconfig.json
└── package.json
```

#### vite.config.ts — Kritik Ayarlar

**Shell (Host) tarafı:**

```typescript
// shell/vite.config.ts
federation({
  name: 'shell',
  remotes: {
    // Her remote'un build edilmiş remoteEntry.js URL'i
    // ÖNEMLİ: /assets/ prefix'i — vite build çıktısı dist/assets/ altına koyar
    remoteCustomers: 'http://localhost:3001/assets/remoteEntry.js',
    remoteAccounts:  'http://localhost:3002/assets/remoteEntry.js',
    remotePayments:  'http://localhost:3003/assets/remoteEntry.js',
    remoteLedger:    'http://localhost:3004/assets/remoteEntry.js',
  },
  shared, // Aşağıda açıklanıyor
})
```

**Remote tarafı:**

```typescript
// remote-customers/vite.config.ts
federation({
  name: 'remoteCustomers',           // Shell'deki remotes key'i ile eşleşmeli
  filename: 'remoteEntry.js',         // Build çıktı dosya adı
  exposes: {
    './CustomersApp': './src/CustomersApp',  // Shell'in import edeceği modül
  },
  shared, // Shell ile AYNI shared config
})
```

#### shared Konfigürasyonu — Neden Önemli?

Module Federation'da her remote kendi `node_modules`'ını bundle eder. `shared` olmadan shell ve remote'lar **farklı React instance'ları** yükler → `useSyncExternalStore null` hatası alırsın.

```typescript
const shared = {
  react:             { singleton: true, requiredVersion: '^18.3.1' },
  'react-dom':       { singleton: true, requiredVersion: '^18.3.1' },
  'react-router-dom':{ singleton: true, requiredVersion: '^6.26.2' },
  '@mui/material':   { singleton: true, requiredVersion: '^5.16.7' },
  '@mui/x-data-grid':{ singleton: true, requiredVersion: '^7.14.0' },
  '@emotion/react':  { singleton: true },
  '@emotion/styled': { singleton: true },
};
```

**`singleton: true` ne yapar?** Tüm microfrontend'lerin aynı kütüphane instance'ını kullanmasını zorlar. Shell kütüphaneyi yükler, remote'lar shell'in yüklediğini kullanır.

**`requiredVersion` ne yapar?** Versiyon uyumsuzluğunda uyarı verir. Shell ve remote'lardaki `package.json` versiyonları eşleşmeli.

**Hangi kütüphaneler shared olmalı?**
- React ve ReactDOM → **zorunlu** (singleton olmazsa hooks çalışmaz)
- react-router-dom → **zorunlu** (shell'deki Router context'i remote'larda kullanılır)
- @mui/material ve @emotion → **önerilir** (bundle boyutunu düşürür, tema tutarlılığı sağlar)
- @mui/x-data-grid → **önerilir** (890KB, tekrar yüklenmesini önler)

#### Başlatma Sırası — Neden Önemli?

```
1. Remote'lar build olur (vite build)
2. Remote'lar preview sunucusu başlatır (vite preview → :3001-3004)
3. Shell, remote'ların remoteEntry.js dosyalarının erişilebilir olmasını bekler (wait-on)
4. Shell build olur ve preview sunucusu başlatır (:3000)
```

Bu sıra `microui/package.json`'daki script'lerle otomatik yönetilir:

```json
{
  "start:remotes": "concurrently \"npm run start --workspace=remote-customers\" ...",
  "start:shell": "wait-on http://localhost:3001/assets/remoteEntry.js ... && npm run start --workspace=shell",
  "start": "concurrently \"npm run start:remotes\" \"npm run start:shell\""
}
```

**Shell remote'u bulamazsa ne olur?** `lazy()` import'u reject olur, React `<Suspense>` fallback gösterir, konsola `Failed to fetch dynamically imported module` hatası düşer.

#### Production vs Development Mode — Kritik Fark

**Tüm projeler `vite build && vite preview` ile çalışır.** Sebebi:

| | `vite` (dev server) | `vite build && vite preview` |
|---|---|---|
| React build | Development | Production |
| remoteEntry.js | Virtual module (çalışmaz) | Statik dosya (`dist/assets/`) |
| Hot reload | Var | Yok |
| Uyumluluk | Shell dev + Remote prod = ÇAKIŞIR | Hepsi prod = ÇALIŞIR |

> **Kural:** Shell ve remote'lar ya hepsi dev ya hepsi prod olmalı. `vite-plugin-federation` iki modu karıştırmayı desteklemez. Bu projede **hepsi production** modu kullanılır.

#### Yeni Remote Ekleme Adımları

Diyelim `remote-reports` adında yeni bir microfrontend eklemek istiyorsun:

**1. Proje oluştur:**
```bash
cd microui
mkdir remote-reports && cd remote-reports
npm init -y
```

**2. Bağımlılıkları ekle** (shell ile aynı versiyonlar):
```bash
npm install react@^18.3.1 react-dom@^18.3.1 react-router-dom@^6.26.2 \
  @emotion/react@^11.13.3 @emotion/styled@^11.13.0 \
  @mui/material@^5.16.7 @mui/icons-material@^5.16.7 @mui/x-data-grid@^7.14.0
npm install -D vite@^5.4.6 @vitejs/plugin-react@^4.3.1 \
  @originjs/vite-plugin-federation@^1.3.5 typescript@^5.5.4
```

**3. `vite.config.ts` oluştur:**
```typescript
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import federation from '@originjs/vite-plugin-federation';

const shared = { /* shell ile AYNI shared config */ };

export default defineConfig({
  plugins: [
    react(),
    federation({
      name: 'remoteReports',
      filename: 'remoteEntry.js',
      exposes: { './ReportsApp': './src/ReportsApp' },
      shared,
    }),
  ],
  server: { port: 3005, cors: true },
  preview: { port: 3005, cors: true },
  build: { target: 'esnext', minify: false, cssCodeSplit: false },
});
```

**4. Shell'e remote'u tanıt** (`shell/vite.config.ts`):
```typescript
remotes: {
  // ...mevcut remote'lar
  remoteReports: 'http://localhost:3005/assets/remoteEntry.js',
},
```

**5. Shell'e route ekle** (`shell/src/App.tsx`):
```typescript
const ReportsApp = lazy(() => import('remoteReports/ReportsApp'));
// ...
<Route path="/reports/*" element={<ReportsApp />} />
```

**6. Root `package.json`'a workspace ve script ekle:**
- `workspaces` dizisine `"remote-reports"` ekle
- `start:remotes`'a yeni workspace'i ekle
- `start:shell`'deki `wait-on`'a `http://localhost:3005/assets/remoteEntry.js` ekle

**7. TypeScript tanımı ekle** (`shell/src/vite-env.d.ts` veya `remotes.d.ts`):
```typescript
declare module 'remoteReports/ReportsApp' {
  const Component: React.ComponentType;
  export default Component;
}
```

#### Sık Yapılan Hatalar ve Çözümleri

| Hata | Sebep | Çözüm |
|---|---|---|
| `useSyncExternalStore is null` | İki farklı React instance yüklendi | `shared`'de `singleton: true` kontrol et. Hepsi prod modda mı? |
| `remoteEntry.js 404` | Remote build olmamış veya URL yanlış | `dist/assets/remoteEntry.js` var mı? URL'de `/assets/` prefix'i var mı? |
| `Failed to fetch dynamically imported module` | Remote sunucu çalışmıyor | Remote'un `vite preview`'ı aktif mi? Port'u doğru mu? |
| `Element type is invalid` | expose path yanlış | Remote'daki `exposes` key'i ile shell'deki `import()` path'i eşleşmeli |
| Tema/stil tutarsızlığı | MUI shared değil veya ThemeProvider yok | Shell'de `ThemeProvider` remote'ları da sarar. `@mui/material` shared olmalı |
| Bundle çok büyük | Shared kütüphaneler her remote'a gömülmüş | `shared` config'i kontrol et, `singleton: true` eksik olabilir |
| `import React` hatası | React 17+ JSX transform | `import React from 'react'` kullanma, sadece hook'ları import et |
| `valueFormatter` çalışmıyor | MUI DataGrid v7 API değişikliği | `(value: number) => ...` kullan, `({ value }) => ...` değil |

#### Vite Versiyon Kısıtlaması

```
@originjs/vite-plugin-federation@1.3.5  →  Vite 4.x / 5.x ile uyumlu
                                         →  Vite 6+ ile UYUMSUZ (Rolldown engine)
```

`package.json`'daki Vite versiyonu `^5.4.6` olmalı. Vite 6+'ya geçiş ancak federation eklentisi güncellendiğinde yapılabilir. Alternatif olarak `@module-federation/vite` eklentisine geçiş düşünülebilir.

#### CORS ve Gateway URL

- Shell `http://localhost:3000`'den backend API'ye (`http://localhost:8080`) istek atar
- Gateway'de CORS `localhost:3000-3004` için açık (`application.yml`)
- Frontend'de gateway URL'i `VITE_GATEWAY_URL` env variable ile değiştirilebilir:
  ```bash
  VITE_GATEWAY_URL=http://api.example.com npm run start
  ```
  Default: `http://localhost:8080`

#### Build Sırası ve Komutlar

| Komut | Ne yapar |
|---|---|
| `npm run start` (root) | Remote'ları build+preview → shell build+preview |
| `npm run build` (root) | Tüm workspace'leri build eder (`dist/` klasörleri oluşur) |
| Remote'da `vite build` | `dist/assets/remoteEntry.js` + chunk dosyaları üretir |
| Shell'de `vite build` | `dist/` altına host bundle üretir (remote'ları runtime'da yükler) |
| `vite preview` | Build çıktısını statik sunucu olarak serve eder |

### Genel

1. **Docker Compose ile başlat:** Tüm altyapı (DB, ELK, backend) tek komutla ayağa kalkar.
2. **Her servisin kendi veritabanı var:** Servisler arası doğrudan DB erişimi yok — sadece API üzerinden iletişim.
3. **Kibana'da logları izle:** `http://localhost:5601` adresinden tüm servis loglarını merkezi olarak izleyebilirsin.
4. **Git'e commit etmeden önce:** `node_modules/`, `dist/`, `target/`, `.idea/` gibi klasörlerin `.gitignore`'da olduğundan emin ol.

---

## Kibana Kurulumu

1. `http://localhost:5601` adresine git
2. **Management > Stack Management > Index Patterns** menüsüne gir
3. `banking-logs-*` pattern'i oluştur
4. **Discover** sekmesinden logları görüntüle

---

## Sık Karşılaşılan Sorunlar

| Sorun | Çözüm |
|---|---|
| `useSyncExternalStore null` hatası | Vite 5.x kullandığından emin ol, `singleton: true` kontrol et |
| `Connection refused` (gateway) | Backend servisleri rebuild sonrası `docker compose restart api-gateway` |
| `CHAR vs VARCHAR` schema hatası | Flyway migration'da `VARCHAR(3)` kullan |
| `remoteEntry.js 404` | Remote'ların build olduğundan emin ol (`dist/assets/remoteEntry.js` mevcut mu?) |
| Frontend açılmıyor | `pkill -f vite && cd microui && npm run start` |
| Local Maven compile hatası | Sistem JDK 25, proje JDK 21. Docker ile build et veya JDK 21 kur |
