# Rent A Car — Backend API

Araç kiralama uygulamasının REST API katmanı. JWT tabanlı kimlik doğrulama, araç listeleme/filtreleme ve rezervasyon yönetimi sunar.

Bu proje, `RentACar` monoreposunun backend bileşenidir. Frontend: [`rent-a-car`](../rent-a-car).

---

## Kullanılan Teknolojiler

| Kategori | Teknoloji |
|----------|-----------|
| Dil | Java 17 |
| Framework | Spring Boot 4.1 |
| API | Spring Web MVC (REST) |
| Güvenlik | Spring Security, JWT (jjwt) |
| Veritabanı | Spring Data JPA, Hibernate |
| DB | PostgreSQL |
| Validasyon | Jakarta Validation |
| Yardımcı | Lombok, Maven |

---

## Gereksinimler

- Java 17+
- Maven 3.8+ (veya projedeki `./mvnw` wrapper)
- PostgreSQL 14+

---

## Kurulum

### 1. Veritabanını oluşturun

```bash
createdb rentacar
```

### 2. Ortam değişkenlerini yapılandırın

Proje kökünde `.env.example` dosyasını `.env` olarak kopyalayın ve değerleri doldurun:

```bash
cp .env.example .env
```

```env
DB_URL=jdbc:postgresql://localhost:5432/rentacar
DB_USERNAME=<kullanici_adi>
DB_PASSWORD=<sifre>
JWT_SECRET=<openssl rand -base64 32 ile üretin>
SPRING_PROFILES_ACTIVE=dev
```

> `.env` repoya commit edilmez. Üretimde `SPRING_PROFILES_ACTIVE=prod` kullanın; `prod` profili `ddl-auto=validate` ile şemayı otomatik değiştirmez.

### 3. Uygulamayı çalıştırın

Monorepo kök dizininden (`RentACar/`):

```bash
cd rent-a-car-backend-my
./mvnw spring-boot:run
```

Zaten `rent-a-car-backend-my` klasöründeyken:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

API varsayılan olarak `http://localhost:8080` adresinde çalışır.

Geliştirme profilinde (`dev`) Hibernate tabloları otomatik oluşturulur (`ddl-auto=update`) ve veritabanı boşsa örnek araç verileri yüklenir.

---

## Testleri Çalıştırma

```bash
./mvnw test
```

---

## Kimlik Doğrulama

Korumalı endpoint'lere erişim için `Authorization` başlığında Bearer token gönderilmelidir:

```
Authorization: Bearer <jwt_token>
```

- `/auth/**` ve `GET /cars/**` endpoint'leri herkese açıktır.
- `/rentals/**` endpoint'leri `ROLE_USER` gerektirir.

CORS, `http://localhost:5173` ve `http://localhost:3000` origin'lerine izin verecek şekilde yapılandırılmıştır.

---

## API Endpoint'leri

### Kimlik Doğrulama (`/auth`)

| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| `POST` | `/auth/register` | Yeni kullanıcı kaydı | Hayır |
| `POST` | `/auth/login` | Giriş yap, JWT al | Hayır |

**Kayıt isteği örneği:**

```json
{
  "name": "Ahmet",
  "surname": "Yılmaz",
  "email": "ahmet@example.com",
  "password": "sifre123",
  "passwordConfirm": "sifre123"
}
```

**Giriş isteği örneği:**

```json
{
  "email": "ahmet@example.com",
  "password": "sifre123"
}
```

**Giriş yanıtı örneği:**

```json
{
  "jwtToken": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1,
  "email": "ahmet@example.com",
  "name": "Ahmet",
  "surname": "Yılmaz"
}
```

---

### Araçlar (`/cars`)

| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| `GET` | `/cars` | Tüm araçları listele | Hayır |
| `GET` | `/cars/featured` | Öne çıkan araçları listele | Hayır |
| `GET` | `/cars/{id}` | Araç detayı | Hayır |
| `GET` | `/cars/filter` | Filtrelenmiş araç listesi | Hayır |

**Filtre parametreleri** (`/cars/filter`):

| Parametre | Tip | Açıklama |
|-----------|-----|----------|
| `startDate` | `YYYY-MM-DD` | Kiralama başlangıç tarihi |
| `endDate` | `YYYY-MM-DD` | Kiralama bitiş tarihi |
| `minPrice` | `double` | Minimum günlük fiyat |
| `maxPrice` | `double` | Maksimum günlük fiyat |
| `city` | `string` | Şehir |
| `make` | `string` | Marka |
| `fuelType` | `string` | Yakıt tipi (`benzin`, `dizel`, `elektrik`, `hybrid`) |
| `transmission` | `string` | Vites (`otomatik`, `manuel`) |
| `seats` | `int` | Koltuk sayısı |
| `isAvailable` | `boolean` | Müsaitlik durumu |
| `isFeatured` | `boolean` | Öne çıkan araçlar |

**Örnek:**

```
GET /cars/filter?city=İstanbul&minPrice=800&maxPrice=1500&fuelType=benzin
```

---

### Rezervasyonlar (`/rentals`)

| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| `POST` | `/rentals` | Yeni rezervasyon oluştur | Evet |
| `GET` | `/rentals` | Oturum açmış kullanıcının rezervasyonları | Evet |
| `GET` | `/rentals/car/{carId}/active` | Araca ait aktif rezervasyonlar | Evet |
| `PUT` | `/rentals/{id}/cancel` | Rezervasyonu iptal et | Evet |

**Rezervasyon oluşturma isteği örneği:**

```json
{
  "carId": 1,
  "startDate": "2026-06-25",
  "endDate": "2026-06-28",
  "location": "İstanbul Havalimanı"
}
```

> Toplam fiyat sunucu tarafında `günlük fiyat × kiralama günü` formülüyle hesaplanır; istemciden gönderilmez.

Rezervasyon durumları: `active`, `cancelled`.

---

## Proje Yapısı

```
src/main/java/com/rentacar/
├── config/          # Security, JWT filter, seed data
├── controller/      # REST controller'lar
├── dto/             # İstek/yanıt modelleri
├── entity/          # JPA entity'leri (User, Car, Rental)
├── enums/           # FuelType, Transmission, RentalStatus
├── exceptions/      # Global exception handler
├── repository/      # Spring Data JPA repository'leri
├── service/         # İş mantığı katmanı
└── utils/           # JWT ve güvenlik yardımcıları
```

---

## Ortam Değişkenleri

| Değişken | Varsayılan | Açıklama |
|----------|------------|----------|
| `DB_URL` | — | PostgreSQL bağlantı URL'i |
| `DB_USERNAME` | — | Veritabanı kullanıcı adı |
| `DB_PASSWORD` | — | Veritabanı şifresi |
| `JWT_SECRET` | — | JWT imzalama anahtarı (en az 256 bit, `openssl rand -base64 32`) |
| `JWT_EXPIRATION_MS` | `36000000` | Token geçerlilik süresi (ms) |
| `SPRING_PROFILES_ACTIVE` | `dev` | `dev`: ddl-auto=update, `prod`: ddl-auto=validate |

---

## Frontend Entegrasyonu

Frontend (`rent-a-car`) Spring Boot REST API ile entegre çalışır. `VITE_API_URL` ortam değişkeni backend adresini belirler (varsayılan: `http://localhost:8080`).

Frontend'i çalıştırmak için:

```bash
cd ../rent-a-car
npm install
npm run dev
```

Frontend `http://localhost:5173` adresinde açılır.
