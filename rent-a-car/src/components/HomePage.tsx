import { useLoaderData, useNavigate } from "react-router-dom";
import type { Car } from "../types/car";
import homeCarImage from "../img/pngwing.com (2).png";
import { getCarImage } from "./utils/carImages";
import { IoCarSport } from "react-icons/io5";
import { IoPricetagsSharp } from "react-icons/io5";
import { IoIosClock } from "react-icons/io";
import { IoShieldCheckmark } from "react-icons/io5";

import { IoSearchOutline } from "react-icons/io5";

const HomePage = () => {
  const navigate = useNavigate();
  const featuredCars = useLoaderData() as Car[];

  const handleSearch = () => {
    navigate("/cars");
  };

  return (
    <>
      <div className="hero-section">
        <div className="text-section">
          <h1 className="hero-title">
            Hayalinizdeki Aracı Kolay Hızlı ve Güvenli Bir Şekilde Kiralayın
          </h1>
          <p className="hero-description">
            Dakikalar içinde rezarvasyon yapın ve hemen yola çıkın!
          </p>
          <div className="button-container">
            <button
              onClick={() => navigate("/cars")}
              className="primary-button"
            >
              Aracınızı Seçin
            </button>
            <button
              onClick={() => navigate("/auth/login")}
              className="secondary-button"
            >
              Giriş Yapın
            </button>
          </div>
        </div>
        <div className="image-section">
          <img
            src={homeCarImage}
            alt="homeCar"
            className="xl:w-2/3 xl:h-full object-cover  xl:mr-10 "
          />
        </div>
      </div>

      <div className="inform-section">
        <div className="max-w-6xl mx-auto">
          <div className="modern-card-bg">
            <div className="reservation-section">
              <div className="text-center mb-8">
                <h2 className="text-4xl font-bold text-gray-900 mb-4">
                  Hızlı Araç Kiralama
                </h2>
                <p className="text-lg text-gray-600 max-w-2xl mx-auto">
                  Sadece birkaç tıklama ile hayalinizdeki aracı bulun ve
                  rezervasyon yapın
                </p>
              </div>

              {/* Ana Arama Butonu */}
              <div className="text-center mb-12">
                <button
                  onClick={handleSearch}
                  className="inline-flex items-center px-10 py-5 bg-gradient-to-r from-orange-600 to-orange-700 text-white font-bold text-xl rounded-xl hover:from-orange-700 hover:to-orange-800 transform hover:scale-105 transition-all duration-200 shadow-lg hover:shadow-xl"
                >
                  <IoSearchOutline className="w-7 h-7 mr-3" />
                  Uygun Araçları Bul
                </button>
              </div>

              {/* İstatistikler Bölümü */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-8 pt-8 border-t border-gray-200">
                <div className="text-center">
                  <div className="text-4xl font-bold text-orange-600 mb-3">
                    200+
                  </div>
                  <div className="text-gray-600 text-lg">Araç Seçeneği</div>
                </div>
                <div className="text-center">
                  <div className="text-4xl font-bold text-orange-600 mb-3">
                    9
                  </div>
                  <div className="text-gray-600 text-lg">Şehir</div>
                </div>
                <div className="text-center">
                  <div className="text-4xl font-bold text-orange-600 mb-3">
                    24/7
                  </div>
                  <div className="text-gray-600 text-lg">Müşteri Desteği</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="featured-cars">
          <h2 className="inform-title">Öne Çıkan Araçlar</h2>
          <div className="cars-grid">
            {featuredCars.map((car: Car) => (
              <div
                onClick={() => navigate(`/cars/carsDetail/${car.id}`)}
                key={car.id}
                className="modern-card-bg cursor-pointer"
              >
                <img
                  src={getCarImage(car.make)}
                  alt={`${car.make} ${car.model}`}
                  className="car-image"
                />
                <div className="car-content">
                  <h3 className="car-title">
                    {car.make} {car.model}
                  </h3>
                  <p className="car-year">Yıl: {car.year}</p>
                  <p className="car-price">₺{car.pricePerDay}/gün</p>
                  <p className="car-details">
                    {car.fuelType} • {car.seats} kişi • {car.transmission}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="icons-section">
          <h2 className="inform-title">Neden Bizi Tercih Etmelisiniz?</h2>
          <div className="icons-grid">
            <div className="icon-card">
              <div className="icon-card-content">
                <IoCarSport />
              </div>
              <h3 className="icon-title">Geniş Araç Filosu</h3>
              <p className="icon-description">
                Geniş araç filosumuz ile size uygun araç bulabilirsiniz.
              </p>
            </div>
            <div className="icon-card">
              <div className="icon-card-content">
                <IoPricetagsSharp />
              </div>
              <h3 className="icon-title"> Uygun Fiyat Garantisi</h3>
              <p className="icon-description">
                En uygun fiyatlarımız ile araç ihtiyacınızı karşılayabiliriz.
              </p>
            </div>
            <div className="icon-card">
              <div className="icon-card-content">
                <IoIosClock />
              </div>
              <h3 className="icon-title"> 7/24 Destek</h3>
              <p className="icon-description">
                Ulaşılabilir hizmetimiz ile her türlü sorununuzu çözebiliriz.
              </p>
            </div>
            <div className="icon-card">
              <div className="icon-card-content">
                <IoShieldCheckmark />
              </div>
              <h3 className="icon-title">Sigortalı Araçlar</h3>
              <p className="icon-description">
                Sigortalı araçlarımızı konforlu ve güvenli bir şekilde
                kiralayabilirsiniz.
              </p>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default HomePage;
