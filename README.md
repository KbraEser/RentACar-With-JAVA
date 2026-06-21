# Rent A Car

Araç kiralama uygulaması. React tabanlı frontend ve Java Spring Boot tabanlı backend.

```
RentACar/
├── rent-a-car/              → Frontend (React + TypeScript)
└── rent-a-car-backend-my/   → Backend (Java + Spring Boot)
```

---

## Kullanılan Teknolojiler

### Frontend (`rent-a-car`)

| Kategori | Teknoloji |
|----------|-----------|
| Framework | React 19, TypeScript |
| Build | Vite |
| Styling | Tailwind CSS, PostCSS |
| State | Redux Toolkit, React Redux |
| Routing | React Router DOM |
| Form | React Hook Form |
| HTTP | Axios |
| Tarih | date-fns, React DatePicker |
| UI | React Icons, React Toastify |
| Araçlar | ESLint, TypeScript ESLint |

### Backend (`rent-a-car-backend-my`)

| Kategori | Teknoloji |
|----------|-----------|
| Dil | Java 17 |
| Framework | Spring Boot |
| API | Spring Web (REST) |
| Güvenlik | Spring Security, JWT |
| Veritabanı | Spring Data JPA, Hibernate |
| DB | PostgreSQL |
| Validasyon | Jakarta Validation |
| Yardımcı | Lombok, Maven |

---

## rent-a-car (Frontend)

Kullanıcıların araç arayıp filtreleyebildiği, rezervasyon yapabildiği ve kendi panelinden kiralamalarını takip edebildiği web arayüzü.

**Çalıştırmak için:**
```bash
cd rent-a-car
npm install
npm run dev
```

Uygulama `http://localhost:5173` adresinde açılır.

---

## rent-a-car-backend-my (Backend)

REST API: kimlik doğrulama (JWT), araç yönetimi ve rezervasyon işlemleri.

**Çalıştırmak için:**
```bash
cd rent-a-car-backend-my
mvn spring-boot:run
```

API `http://localhost:8080` adresinde çalışır. PostgreSQL'de `rentacar` veritabanının oluşturulmuş olması gerekir.

**Temel endpoint'ler:**
- `POST /auth/register`, `POST /auth/login`
- `GET /cars`, `GET /cars/filter`, `GET /cars/featured`
- `POST /rentals`, `GET /rentals`, `PUT /rentals/{id}/cancel`

---

## Genel Not

Frontend şu an Supabase üzerinden veri alıyor. Backend hazır; tam entegrasyon için frontend servis katmanının REST API'ye yönlendirilmesi yeterli.
