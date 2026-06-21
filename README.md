# Rent A Car

Araç kiralama uygulamasının React tabanlı frontend projesi.

```
RentACar/
└── rent-a-car/  → Frontend (React + TypeScript)
```

---

## Kullanılan Teknolojiler

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

## Genel Not

Frontend şu an Supabase üzerinden veri alıyor. Java Spring Boot backend ayrı bir projede geliştirilmektedir.
