import { useState, useCallback, useRef, useEffect } from "react";
import type { Car } from "../../types/car";
import { fetchFilteredCars, type CarFilters } from "../../services/carService";
import type { FilterFormData } from "../../components/car/CarFilterForm";
import { handleAndShowError } from "../../utils/errorHandler";

interface UseCarFilterProps {
  initialCars: Car[];
}

export function useCarFilter({ initialCars }: UseCarFilterProps) {
  const [cars, setCars] = useState<Car[]>(initialCars);
  const [loading, setLoading] = useState(false);
  const [selectedDateRange, setSelectedDateRange] = useState<{
    startDate: string;
    endDate: string;
  }>({ startDate: "", endDate: "" });

  // initialCars'ı her değiştiğinde güncel tut
  const initialCarsRef = useRef(initialCars);
  initialCarsRef.current = initialCars;

  const applyFilters = useCallback(async (formData: FilterFormData) => {
    setLoading(true);
    try {
      // Filtreleri hazırla
      const filters: CarFilters = {
        startDate: formData.startDate,
        endDate: formData.endDate,
      };

      // Şehir filtresi
      if (formData.city && formData.city !== "") {
        filters.city = formData.city;
      }

      // Marka filtresi
      if (formData.make && formData.make !== "") {
        filters.make = formData.make;
      }

      // Yakıt türü filtresi
      if (formData.fuel_type && formData.fuel_type !== "") {
        filters.fuelType = formData.fuel_type;
      }

      // Vites türü filtresi
      if (formData.transmission && formData.transmission !== "") {
        filters.transmission = formData.transmission;
      }

      // Fiyat aralığı filtresi
      if (formData.priceRange && formData.priceRange !== "") {
        const priceRange = formData.priceRange;

        if (priceRange === "200-999") {
          filters.minPrice = 200;
        } else {
          const [minPrice, maxPrice] = priceRange.split("-").map(Number);
          filters.minPrice = minPrice;
          filters.maxPrice = maxPrice;
        }
      }

      // Tarih aralığını güncelle
      setSelectedDateRange({
        startDate: formData.startDate || "",
        endDate: formData.endDate || "",
      });

      if (Object.keys(filters).length > 0) {
        const filteredCars = await fetchFilteredCars(filters);

        setCars(filteredCars);
      } else {
        // Filtre yoksa başlangıç araçlarını göster
        setCars(initialCarsRef.current);
      }
    } catch (error) {
      handleAndShowError(error, "useCarFilter.applyFilters");
      setCars(initialCarsRef.current);
    } finally {
      setLoading(false);
    }
  }, []);

  const clearFilters = useCallback(() => {
    setCars(initialCarsRef.current);
    setSelectedDateRange({ startDate: "", endDate: "" });
  }, []);

  // initialCars değiştiğinde cars state'ini güncelle (loader'dan yeni veri geldiğinde)
  useEffect(() => {
    setCars(initialCars);
  }, [initialCars]);

  return {
    cars,
    loading,
    selectedDateRange,
    applyFilters,
    clearFilters,
  };
}
