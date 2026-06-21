# 🚗 Rent A Car - Araç Kiralama Sistemi

Modern ve kullanıcı dostu araç kiralama web uygulaması. React 19, TypeScript ve Spring Boot REST API ile geliştirilmiştir.

## ✨ Özellikler

- **🔐 Kimlik Doğrulama**: Kullanıcı kaydı, giriş/çıkış
- **🚗 Araç Yönetimi**: Araç listesi, detaylar, gelişmiş filtreleme
- **📅 Rezervasyon Sistemi**: Tarih seçimi, rezervasyon yönetimi
- **🎛️ Dashboard**: Kullanıcı paneli ve rezervasyon takibi
- **📱 Responsive Tasarım**: Tüm cihazlarda mükemmel görünüm

## 🛠️ Teknoloji Stack

### Frontend

- **React 19**: Modern React özellikleri ve hooks
- **TypeScript**: Tip güvenliği ve geliştirici deneyimi
- **Vite**: Hızlı build tool ve dev server
- **Tailwind CSS**: Utility-first CSS framework
- **React Router DOM**: Client-side routing

### State Management

- **Redux Toolkit**: Modern Redux kullanımı
- **React Redux**: React-Redux entegrasyonu

### Backend

- **Spring Boot**: REST API
- **PostgreSQL**: İlişkisel veritabanı
- **JWT**: Kimlik doğrulama

### UI/UX Libraries

- **React Icons**: Icon kütüphanesi
- **React Hook Form**: Form yönetimi ve validasyon
- **React Toastify**: Bildirim sistemi
- **React DatePicker**: Tarih seçici
- **Date-fns**: Tarih işlemleri

## 🚀 Kurulum

1. **Projeyi klonlayın**

```bash
git clone <repository-url>
cd rent-a-car
```

2. **Bağımlılıkları yükleyin**

```bash
npm install
```

3. **Çevre değişkenlerini ayarlayın**
   `.env.local` dosyası oluşturun:

```env
VITE_API_URL=http://localhost:8080
```

4. **Uygulamayı başlatın**

```bash
npm run dev
```

## 📖 Kullanım

### Ana Sayfa

- Öne çıkan araçları görüntüleme
- Hızlı araç arama butonu
- Uygulama özelliklerini keşfetme

### Araç Arama ve Filtreleme

- **Filtreler**: Tarih, fiyat, şehir, marka, yakıt türü, vites, koltuk sayısı
- **Arama**: Gelişmiş filtreleme sistemi
- **Detaylar**: Araç özellikleri ve görselleri

### Rezervasyon Süreci

1. İstediğiniz aracı seçin
2. Başlangıç ve bitiş tarihlerini belirleyin
3. Teslimat lokasyonunu girin
4. Rezervasyonu onaylayın

### Dashboard

- **Rezervasyonlarım**: Tüm rezervasyonları görüntüleme
- **Yeni Rezervasyon**: Hızlı rezervasyon yapma
- **Profil**: Hesap bilgileri (geliştiriliyor)

## 🗄️ Veritabanı

PostgreSQL veritabanı Spring Boot backend tarafından yönetilir:

### Tablolar

- **users**: Kullanıcı bilgileri (id, name, surname, email)
- **cars**: Araç bilgileri (marka, model, yıl, fiyat, özellikler)
- **rentals**: Rezervasyon kayıtları (tarih, fiyat, durum)

### Güvenlik

- **Spring Security**: Endpoint koruması
- **JWT Tokens**: Güvenli kimlik doğrulama
- **JPA/Hibernate**: Parametreli sorgular ile SQL injection koruması

## 🔄 Geliştirme Durumu

### ✅ Tamamlanan

- Kimlik doğrulama sistemi
- Araç listesi ve detayları
- Rezervasyon sistemi
- Dashboard ve kullanıcı paneli
- Responsive tasarım

### 🚧 Geliştiriliyor

- Profil yönetimi
- Ödeme entegrasyonu

### 📋 Gelecek

- Admin paneli
- Mobil uygulama (React Native)
- Çoklu dil desteği
- Ödeme entegrasyonu (Stripe/PayPal)
- Email bildirimleri
- Araç yorumları ve puanlama

## 🏗️ Proje Yapısı

```
src/
├── components/          # React bileşenleri
│   ├── car/            # Araç bileşenleri
│   ├── common/         # Ortak bileşenler
│   ├── dashboard/      # Dashboard bileşenleri
│   └── utils/          # Yardımcı fonksiyonlar
├── layouts/            # Layout bileşenleri
├── routes/             # Routing yapısı
├── services/           # API servisleri
├── store/              # Redux store
├── types/              # TypeScript tipleri
└── utils/              # Yardımcı fonksiyonlar
```

## 🔧 Geliştirme Komutları

```bash
# Geliştirme sunucusunu başlat
npm run dev

# Production build
npm run build

# Linting
npm run lint

# Preview
npm run preview
```

## 📱 Responsive Tasarım

- **Mobile First**: Mobil cihazlar öncelikli tasarım
- **Breakpoints**: sm (640px), md (768px), lg (1024px), xl (1280px)
- **Modern UI**: Tailwind CSS ile şık tasarım

## 🔒 Güvenlik Özellikleri

- **Input Validation**: Form doğrulama
- **Error Handling**: Güvenli hata yönetimi
- **Protected Routes**: Yetkilendirme kontrolü
- **SQL Injection**: JPA parametreli sorgular ile koruma
- **XSS Protection**: React'in built-in koruması
