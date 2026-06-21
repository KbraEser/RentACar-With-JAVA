import { useLoaderData, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import type { Car } from "../types/car";
import type { RootState } from "../store/store";
import { getCarImage } from "./utils/carImages";
import { useState } from "react";
import {
  IoArrowBack,
  IoPeople,
  IoLocation,
  IoShieldCheckmark,
  IoTime,
} from "react-icons/io5";
import { FaGasPump, FaCog } from "react-icons/fa";

const CarDetailsModal = () => {
  const car = useLoaderData() as Car;
  const navigate = useNavigate();
  const user = useSelector((state: RootState) => state.auth.user);
  const [imageError, setImageError] = useState(false);

  if (!car) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">
            Araç Bulunamadı
          </h2>
          <p className="text-gray-600 mb-6">
            Aradığınız araç bulunamadı veya kaldırılmış olabilir.
          </p>
          <button onClick={() => navigate("/cars")} className="primary-button">
            Araçlara Dön
          </button>
        </div>
      </div>
    );
  }

  const handleReservationClick = () => {
    if (!user) {
      navigate("/auth/login");
      return;
    }
    navigate(`/dashboard/reservationForm/${car.id}`);
  };

  const handleBackClick = () => {
    navigate(-1);
  };

  return (
    <div className="min-h-screen bg-gray-50 py-6 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="mb-6">
          <button
            onClick={handleBackClick}
            className="flex items-center text-gray-600 hover:text-orange-600 transition-colors mb-4"
          >
            <IoArrowBack className="w-5 h-5 mr-2" />
            Geri Dön
          </button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Sol Kolon - Araç Görseli */}
          <div className="lg:col-span-2">
            <div className="bg-white rounded-lg shadow-lg overflow-hidden">
              <div className="aspect-w-14 aspect-h-9 bg-gray-100">
                <img
                  src={
                    imageError ? getCarImage("default") : getCarImage(car.make)
                  }
                  alt={`${car.make} ${car.model}`}
                  className="w-full h-64 sm:h-80 lg:h-96 object-contain p-4"
                  onError={() => setImageError(true)}
                />
              </div>
            </div>

            {/* Araç Özellikleri */}
            <div className="bg-white rounded-lg shadow-lg p-6 mt-6">
              <div className="mb-4">
                <h2 className="text-3xl font-bold text-gray-900 mb-2">
                  {car.make} {car.model} - {car.year} Model
                </h2>
              </div>
              <h3 className="text-2xl font-bold text-gray-800 mb-4">
                Araç Özellikleri
              </h3>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                <div className="flex items-center space-x-3">
                  <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
                    <FaGasPump className="w-5 h-5 text-orange-600" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Yakıt Türü</p>
                    <p className="font-semibold text-gray-800">
                      {car.fuelType}
                    </p>
                  </div>
                </div>

                <div className="flex items-center space-x-3">
                  <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
                    <FaCog className="w-5 h-5 text-orange-600" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Vites</p>
                    <p className="font-semibold text-gray-800">
                      {car.transmission}
                    </p>
                  </div>
                </div>

                <div className="flex items-center space-x-3">
                  <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
                    <IoPeople className="w-5 h-5 text-orange-600" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Kapasite</p>
                    <p className="font-semibold text-gray-800">
                      {car.seats} Kişi
                    </p>
                  </div>
                </div>

                <div className="flex items-center space-x-3">
                  <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
                    <IoLocation className="w-5 h-5 text-orange-600" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Şehir</p>
                    <p className="font-semibold text-gray-800">{car.city}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Sağ Kolon - Rezervasyon Kartı */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-lg p-6 h-fit">
              <div className="text-center mb-6">
                <div className="text-4xl font-bold text-orange-600 mb-2">
                  {car.pricePerDay}₺
                </div>
                <div className="text-gray-500">günlük kiralama</div>
              </div>

              <div className="space-y-4 mb-6">
                <div className="flex justify-between items-center py-2 border-b border-gray-200">
                  <span className="text-gray-600">Durum</span>
                  <span
                    className={`px-3 py-1 rounded-full text-sm font-medium ${
                      car.available
                        ? "bg-green-100 text-green-800"
                        : "bg-red-100 text-red-800"
                    }`}
                  >
                    {car.available ? "Müsait" : "Dolu"}
                  </span>
                </div>

                <div className="flex justify-between items-center py-2 border-b border-gray-200">
                  <span className="text-gray-600">Öne Çıkan</span>
                  <span
                    className={`px-3 py-1 rounded-full text-sm font-medium ${
                      car.featured
                        ? "bg-blue-100 text-blue-800"
                        : "bg-gray-100 text-gray-800"
                    }`}
                  >
                    {car.featured ? "Evet" : "Hayır"}
                  </span>
                </div>

                <div className="flex justify-between items-center py-2 border-b border-gray-200">
                  <span className="text-gray-600">Model Yılı</span>
                  <span className="font-semibold">{car.year}</span>
                </div>

                <div className="flex justify-between items-center py-2 border-b border-gray-200">
                  <span className="text-gray-600">Marka</span>
                  <span className="font-semibold">{car.make}</span>
                </div>
              </div>

              {car.description && (
                <div className="mb-6">
                  <h3 className="text-lg font-semibold text-gray-800 mb-3">
                    Açıklama
                  </h3>
                  <p className="text-sm text-gray-600 leading-relaxed">
                    {car.description}
                  </p>
                </div>
              )}

              <div className="space-y-3">
                <button
                  onClick={handleReservationClick}
                  className={`w-full py-3 px-4 rounded-md font-medium transition-colors ${
                    car.available
                      ? "primary-button"
                      : "bg-gray-400 text-white cursor-not-allowed"
                  }`}
                  disabled={!car.available}
                >
                  {!user
                    ? "Giriş Yap ve Rezervasyon Yap"
                    : car.available
                    ? "Rezervasyon Yap"
                    : "Müsait Değil"}
                </button>

                <button
                  onClick={() => navigate("/cars")}
                  className="w-full secondary-button"
                >
                  Diğer Araçları Gör
                </button>
              </div>

              {/* Güvenlik Bilgileri */}
              <div className="mt-6 pt-6 border-t border-gray-200">
                <div className="flex items-center space-x-2 text-sm text-gray-500">
                  <IoShieldCheckmark className="w-4 h-4 text-green-500" />
                  <span>Güvenli Ödeme</span>
                </div>
                <div className="flex items-center space-x-2 text-sm text-gray-500 mt-1">
                  <IoTime className="w-4 h-4 text-green-500" />
                  <span>7/24 Destek</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CarDetailsModal;
