import { toast } from "react-toastify";
import { useLoaderData, useNavigate } from "react-router-dom";
import { getCarImage } from "../../utils/carImages";
import type { Car } from "../../../types/car";
import { DELIVERY_LOCATIONS, TIME_CONSTANTS } from "../../../constants";

import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { validateAndResetEndDate } from "../../utils/dataUtils";
import { createReservation } from "../../../store/slices/rentalsSlice";
import { useAppDispatch } from "../../../app/hooks/storeHooks";
import { handleAndShowError } from "../../../utils/errorHandler";
import DatePicker from "./DatePicker";

export interface ReservationFormData {
  startDate: string;
  endDate: string;
  totalPrice: number;
  deliveryLocation: string;
}

const ReservationForm = () => {
  const carData = useLoaderData();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const savedFilters = JSON.parse(localStorage.getItem("filters") || "{}");
  const hasFilters = savedFilters.startDate && savedFilters.endDate;
  const { startDate: savedStartDate, endDate: savedEndDate } = hasFilters
    ? savedFilters
    : { startDate: "", endDate: "" };

  const { register, watch, setValue } = useForm<ReservationFormData>({
    defaultValues: {
      startDate: savedStartDate || "",
      endDate: savedEndDate || "",
      totalPrice: 0,
      deliveryLocation: "",
    },
  });

  const car = carData as Car;
  const totalPrice = watch("totalPrice");
  const deliveryLocation = watch("deliveryLocation");
  const startDate = watch("startDate");
  const endDate = watch("endDate");

  useEffect(() => {
    if (!car) return; // Early return inside useEffect is OK

    validateAndResetEndDate(startDate, endDate, setValue);

    if (endDate && startDate && endDate >= startDate) {
      const totalPrice =
        car.pricePerDay *
        ((new Date(endDate).getTime() - new Date(startDate).getTime()) /
          TIME_CONSTANTS.MILLISECONDS_IN_DAY);
      setValue("totalPrice", totalPrice);
    }
  }, [startDate, endDate, car?.pricePerDay, setValue, car]);

  // Car veya city null ise early return
  if (!car) {
    return <div>Araç bilgisi yüklenemedi</div>;
  }

  return (
    <div className="reservation-container">
      <div className="reservation-card">
        <h1 className="reservation-title">Rezervasyon Formu</h1>

        <div className="reservation-grid">
          <div className="reservation-image-container">
            <img
              src={getCarImage(car.make)}
              alt={`${car.make} ${car.model}`}
              className="reservation-image"
            />
          </div>

          <div className="reservation-content">
            <div>
              <h2 className="reservation-car-title">
                {car.make} {car.model}
              </h2>
              <p className="reservation-car-year">{car.year} Model</p>
            </div>

            <div className="reservation-details">
              <div className="reservation-detail-row">
                <span className="reservation-detail-label">Yakıt Türü:</span>
                <span className="reservation-detail-value">
                  {car.fuelType}
                </span>
              </div>
              <div className="reservation-detail-row">
                <span className="reservation-detail-label">Vites:</span>
                <span className="reservation-detail-value">
                  {car.transmission}
                </span>
              </div>
              <div className="reservation-detail-row">
                <span className="reservation-detail-label">Şehir:</span>
                <span className="reservation-detail-value">{car.city}</span>
              </div>
              <div className="reservation-detail-row">
                <span className="reservation-detail-label">Kapasite:</span>
                <span className="reservation-detail-value">
                  {car.seats} Kişi
                </span>
              </div>
              <div className="reservation-detail-row">
                <span className="reservation-detail-label">Açıklama:</span>
                <span className="reservation-detail-value">
                  {car.description}
                </span>
              </div>
            </div>

            <div className="reservation-price-container">
              <div className="text-center">
                <p className="reservation-price-label">Toplam Fiyat</p>
                <p className="reservation-price-value">{totalPrice}₺</p>
                <p className="reservation-price-per-day">
                  {car.pricePerDay}₺/gün
                </p>
              </div>
            </div>

            <div className="reservation-form-section">
              <h3 className="reservation-form-title">Rezervasyon Bilgileri</h3>

              <form className="space-y-4">
                <DatePicker
                  register={register}
                  watch={watch}
                  setValue={setValue}
                  carId={String(car.id)}
                />

                <div>
                  <label className="reservation-form-label">Teslim Yeri</label>
                  <select
                    className="reservation-form-select"
                    {...register("deliveryLocation")}
                  >
                    <option value="">Lokasyon seçin</option>
                    {DELIVERY_LOCATIONS.map((location) => (
                      <option key={location.value} value={location.value}>
                        {location.label}
                      </option>
                    ))}
                  </select>
                </div>

                <button
                  type="button"
                  className="w-full bg-orange-600 text-white py-3 px-4 rounded-md hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-orange-500 font-medium"
                  disabled={!startDate || !endDate || !totalPrice || !deliveryLocation}
                  onClick={async () => {
                    if (!deliveryLocation) {
                      toast.error("Lütfen teslim yeri seçin.");
                      return;
                    }

                    try {
                      await dispatch(
                        createReservation({
                          carId: car.id,
                          startDate,
                          endDate,
                          totalPrice,
                          location: deliveryLocation,
                        })
                      ).unwrap();

                      toast.success("Rezervasyon başarıyla oluşturuldu.");
                      navigate("/dashboard/reservationSuccess", {
                        state: {
                          city: car.city,
                          location: deliveryLocation,
                        },
                      });
                    } catch (error) {
                      handleAndShowError(error, "ReservationForm.onClick");
                    }
                  }}
                >
                  Rezervasyonu Tamamla
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReservationForm;
