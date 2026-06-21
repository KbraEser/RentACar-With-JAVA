import { useForm } from "react-hook-form";

import {
  CITIES,
  CAR_MAKES,
  FUEL_TYPES,
  TRANSMISSION_TYPES,
  PRICE_RANGES,
} from "../../constants";
import { useEffect, useRef } from "react";
import {
  getMinDate,
  getTodayString,
  validateAndResetEndDate,
} from "../utils/dataUtils";

export interface FilterFormData {
  startDate: string;
  endDate: string;
  city: string;
  make: string;
  fuel_type: string;
  priceRange: string;
  transmission: string;
}

interface CarFilterFormProps {
  onFiltersChange: (filters: FilterFormData) => void;
  onClearFilters: () => void;
  loading?: boolean;
}

export default function CarFilterForm({
  onFiltersChange,
  onClearFilters,
  loading = false,
}: CarFilterFormProps) {
  const { register, reset, watch, setValue } = useForm<FilterFormData>({
    defaultValues: {
      startDate: "",
      endDate: "",
      city: "",
      make: "",
      fuel_type: "",
      priceRange: "",
      transmission: "",
    },
  });

  // Her bir değeri ayrı ayrı izle (obje referans problemi çözümü)
  const startDate = watch("startDate");
  const endDate = watch("endDate");
  const city = watch("city");
  const make = watch("make");
  const fuelType = watch("fuel_type");
  const priceRange = watch("priceRange");
  const transmission = watch("transmission");

  const onFiltersMChangeRef = useRef(onFiltersChange);
  const onClearFiltersRef = useRef(onClearFilters);

  onFiltersMChangeRef.current = onFiltersChange;
  onClearFiltersRef.current = onClearFilters;

  // Debounce için timer ref
  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    validateAndResetEndDate(startDate, endDate, setValue);
    // Önceki timer'ı temizle
    if (debounceTimerRef.current) {
      clearTimeout(debounceTimerRef.current);
    }

    // 500ms bekle, ardından filtreleri uygula
    debounceTimerRef.current = setTimeout(() => {
      const formData: FilterFormData = {
        startDate,
        endDate,
        city,
        make,
        fuel_type: fuelType,
        priceRange,
        transmission,
      };

      const hasAnyValue = Object.values(formData).some(
        (value) => value && value.toString().trim() !== ""
      );

      if (hasAnyValue) {
        onFiltersMChangeRef.current(formData);
        localStorage.setItem("filters", JSON.stringify(formData));
      }
    }, 500);

    // Cleanup
    return () => {
      if (debounceTimerRef.current) {
        clearTimeout(debounceTimerRef.current);
      }
    };
  }, [startDate, endDate, city, make, fuelType, priceRange, transmission, setValue]);

  // const handleApplyFilters = () => {
  //   onFiltersChange(getValues());
  // };

  const handleClearFilters = () => {
    reset();
    onClearFilters();
  };
  return (
    <form className="bg-white p-6 rounded-lg shadow-lg mb-8">
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
        <input
          {...register("startDate")}
          type="date"
          placeholder="Başlangıç Tarihi"
          className="search-input"
          min={getTodayString()}
        />

        <input
          {...register("endDate")}
          type="date"
          placeholder="Bitiş Tarihi"
          className="search-input"
          min={getMinDate(watch("startDate"))}
        />

        <select {...register("city")} className="search-input">
          <option value="">Lokasyon Seçin</option>
          {CITIES.map((city) => (
            <option key={city.value} value={city.value}>
              {city.label}
            </option>
          ))}
        </select>

        <select {...register("make")} className="search-input">
          <option value="">Araç Markası</option>
          {CAR_MAKES.map((make) => (
            <option key={make.value} value={make.value}>
              {make.label}
            </option>
          ))}
        </select>

        <select {...register("fuel_type")} className="search-input">
          <option value="">Yakıt Tipi</option>
          {FUEL_TYPES.map((fuel) => (
            <option key={fuel.value} value={fuel.value}>
              {fuel.label}
            </option>
          ))}
        </select>

        <select {...register("transmission")} className="search-input">
          <option value="">Vites Tipi</option>
          {TRANSMISSION_TYPES.map((transmission) => (
            <option key={transmission.value} value={transmission.value}>
              {transmission.label}
            </option>
          ))}
        </select>

        <select {...register("priceRange")} className="search-input">
          <option value="">Fiyat Aralığı</option>
          {PRICE_RANGES.map((price) => (
            <option key={price.value} value={price.value}>
              {price.label}
            </option>
          ))}
        </select>

        <button
          type="button"
          className="primary-button"
          disabled={loading}
          onClick={handleClearFilters}
        >
          {loading ? "Yükleniyor..." : "Temizle"}
        </button>
      </div>
    </form>
  );
}
