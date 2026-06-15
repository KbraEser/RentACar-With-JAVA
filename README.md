# Rent A Car

Araç kiralama uygulaması. Proje iki ana parçadan oluşur: React tabanlı frontend ve Java Spring Boot tabanlı backend.

```
RentACar/
├── rent-a-car/          → Frontend (React + TypeScript)
└── rent-a-car-backend/  → Backend (Java + Spring Boot)
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
| Backend (mevcut) | Supabase |
| Araçlar | ESLint, TypeScript ESLint |

### Backend (`rent-a-car-backend`)

| Kategori | Teknoloji |
|----------|-----------|
| Dil | Java 17 |
| Framework | Spring Boot 3.4 |
| API | Spring Web (REST) |
| Güvenlik | Spring Security, JWT (jjwt) |
| Veritabanı | Spring Data JPA, Hibernate |
| DB | PostgreSQL |
| Validasyon | Jakarta Validation |
| Yardımcı | Lombok, Maven |
| Test | Spring Boot Test, Spring Security Test |

---

## rent-a-car (Frontend)

Kullanıcıların araç arayıp filtreleyebildiği, rezervasyon yapabildiği ve kendi panelinden kiralamalarını takip edebildiği web arayüzü.

**Temel özellikler:**
- Kullanıcı kaydı ve giriş
- Araç listeleme, filtreleme ve detay görüntüleme
- Rezervasyon oluşturma ve iptal
- Kullanıcı dashboard'u

**Çalıştırmak için:**
```bash
cd rent-a-car
npm install
npm run dev
```

Uygulama `http://localhost:5173` adresinde açılır.

> Detaylı bilgi için: [rent-a-car/README.md](rent-a-car/README.md)

---

## rent-a-car-backend (Backend)

Frontend'in ihtiyaç duyduğu REST API'yi sunan Java backend. Kimlik doğrulama (JWT), araç yönetimi ve rezervasyon işlemlerini karşılar.

**Temel özellikler:**
- Kayıt ve giriş (`/auth`)
- Araç listeleme ve filtreleme (`/cars`)
- Rezervasyon CRUD işlemleri (`/rentals`)

**Çalıştırmak için:**
```bash
cd rent-a-car-backend
mvn spring-boot:run
```

API `http://localhost:8080` adresinde çalışır. PostgreSQL'de `rentacar` veritabanının oluşturulmuş olması gerekir.

> Detaylı bilgi için: [rent-a-car-backend/README.md](rent-a-car-backend/README.md)

---

## Genel Not

Frontend şu an Supabase üzerinden veri alıyor. Backend hazır durumda; iki tarafın tam entegrasyonu için frontend servis katmanının REST API'ye yönlendirilmesi yeterli.
