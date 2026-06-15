# Rent A Car Backend

Twitter clone projesindeki mimariyi baz alan Spring Boot REST API.

## Teknolojiler

- Java 17
- Spring Boot 3.4
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL

## Kurulum

1. PostgreSQL'de veritabanı oluşturun:

```sql
CREATE DATABASE rentacar;
```

2. `src/main/resources/application.properties` dosyasındaki veritabanı bilgilerini güncelleyin.

3. Uygulamayı çalıştırın:

```bash
cd rent-a-car-backend
mvn spring-boot:run
```

API `http://localhost:8080` adresinde çalışır.

## API Endpoints

### Auth (herkese açık)

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/auth/register` | Kayıt ol |
| POST | `/auth/login` | Giriş yap (JWT döner) |

### Cars (herkese açık)

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/cars` | Tüm araçlar |
| GET | `/cars/featured` | Öne çıkan araçlar |
| GET | `/cars/{id}` | Araç detayı |
| GET | `/cars/filter` | Filtrelenmiş araçlar |

Filtre parametreleri: `startDate`, `endDate`, `minPrice`, `maxPrice`, `city`, `make`, `fuelType`, `transmission`, `seats`, `isAvailable`, `isFeatured`

### Rentals (JWT gerekli)

Header: `Authorization: Bearer <token>`

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/rentals` | Rezervasyon oluştur |
| GET | `/rentals` | Kullanıcının rezervasyonları |
| GET | `/rentals/car/{carId}/active` | Aracın aktif rezervasyonları |
| PUT | `/rentals/{id}/cancel` | Rezervasyon iptal |

## Proje Yapısı

```
src/main/java/com/rentacar/
├── config/          # Security, JWT, CORS, seed data
├── controller/      # REST endpoint'leri
├── dto/             # Request/Response modelleri
├── entity/          # JPA entity'leri
├── exceptions/      # Hata yönetimi
├── repository/      # Veritabanı erişimi
├── service/         # İş mantığı
└── util/            # JWT ve security yardımcıları
```

## Örnek İstekler

**Kayıt:**
```json
POST /auth/register
{
  "name": "Kübra",
  "surname": "Eser",
  "email": "kubra@example.com",
  "password": "123456",
  "password_confirm": "123456"
}
```

**Giriş:**
```json
POST /auth/login
{
  "email": "kubra@example.com",
  "password": "123456"
}
```

**Rezervasyon:**
```json
POST /rentals
{
  "car_id": 1,
  "start_date": "2026-06-20",
  "end_date": "2026-06-25",
  "total_price": 4250,
  "location": "İstanbul Havalimanı"
}
```
