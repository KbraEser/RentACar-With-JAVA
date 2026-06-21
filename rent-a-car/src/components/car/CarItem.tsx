import { useNavigate } from "react-router-dom";
import type { Car } from "../../types/car";
import { getCarImage } from "../utils/carImages";

interface CarItemProps {
  car: Car;
}

export default function CarItem({ car }: CarItemProps) {
  const navigate = useNavigate();
 
  const handleCardClick = () => {
    navigate(`/cars/carsDetail/${car.id}`);
  };

  const handleReservationClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    navigate(`/dashboard/reservationForm/${car.id}`);
  };

  return (
    <div
      onClick={handleCardClick}
      className="bg-white rounded-lg shadow-lg overflow-hidden cursor-pointer hover:shadow-xl transition-shadow duration-300"
    >
      <img
        src={getCarImage(car.make)}
        alt={`${car.make} ${car.model}`}
        className="w-2/3 h-34 md:w-full md:h-48 object-cover pl-6"
      />
      <div className="p-4">
        <h3 className="text-lg font-semibold">
          {car.make} {car.model}
        </h3>
        <p className="text-gray-600">{car.year}</p>
        <p className="text-xl font-bold text-blue-600">
          {car.pricePerDay}₺/gün
        </p>
        <p className="text-sm text-gray-500 mb-4">
          {car.fuelType} • {car.seats} kişi • {car.transmission}
        </p>
        <p className="text-sm text-gray-500 mb-4">{car.city}</p>
        <div className="flex gap-2 ">
          <button className="primary-button flex-1">Detaylı Bilgi</button>
          <button
            onClick={handleReservationClick}
            className="secondary-button flex-1"
          >
            Rezervasyon
          </button>
        </div>
      </div>
    </div>
  );
}
