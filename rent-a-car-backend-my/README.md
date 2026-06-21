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

### 2. Bağlantı ayarlarını yapılandırın

`src/main/resources/application.properties` dosyasını kendi ortamınıza göre düzenleyin:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rentacar
spring.datasource.username=<kullanici_adi>
spring.datasource.password=<sifre>

jwt.secret=<gizli-anahtar>
jwt.expiration-ms=36000000
```

> Üretim ortamında `jwt.secret` değerini mutlaka güçlü ve benzersiz bir anahtarla değiştirin.

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

İlk başlatmada Hibernate tabloları otomatik oluşturulur (`spring.jpa.hibernate.ddl-auto=update`) ve veritabanı boşsa örnek araç verileri yüklenir.

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
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "id": 1,
  "email": "ahmet@example.com",
  "name": "Ahmet",
  "password": "..."
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
  "totalPrice": 2550.00,
  "location": "İstanbul Havalimanı"
}
```

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

| Anahtar | Varsayılan | Açıklama |
|---------|------------|----------|
| `server.port` | `8080` | API portu |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/rentacar` | PostgreSQL bağlantı URL'i |
| `spring.datasource.username` | — | Veritabanı kullanıcı adı |
| `spring.datasource.password` | — | Veritabanı şifresi |
| `jwt.secret` | — | JWT imzalama anahtarı |
| `jwt.expiration-ms` | `36000000` | Token geçerlilik süresi (ms) |

---

## Frontend Entegrasyonu

Frontend (`rent-a-car`) şu an Supabase üzerinden veri alıyor. Bu backend hazır durumda; tam entegrasyon için frontend servis katmanının `http://localhost:8080` adresindeki REST API'ye yönlendirilmesi yeterlidir.

Frontend'i çalıştırmak için:

```bash
cd ../rent-a-car
npm install
npm run dev
```

Frontend `http://localhost:5173` adresinde açılır.
