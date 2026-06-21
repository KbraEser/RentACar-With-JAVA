import axios from "axios";
import { apiClient } from "../lib/apiClient";
import type { Car } from "../types/car";
import { getApiErrorMessage, validateCarId } from "../utils/errorHandler";

export interface CarFilters {
  startDate?: string;
  endDate?: string;
  minPrice?: number;
  maxPrice?: number;
  city?: string;
  make?: string;
  fuelType?: string;
  transmission?: string;
  seats?: number;
  isAvailable?: boolean;
  isFeatured?: boolean;
}

export const fetchAllCars = async (): Promise<Car[]> => {
  try {
    const { data } = await apiClient.get<Car[]>("/cars");
    return data;
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "CarService.fetchAllCars"));
  }
};

export const fetchFilteredCars = async (filters: CarFilters): Promise<Car[]> => {
  try {
    const { data } = await apiClient.get<Car[]>("/cars/filter", { params: filters });
    return data;
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "CarService.fetchFilteredCars"));
  }
};

export const fetchCarById = async (id: string): Promise<Car | null> => {
  validateCarId(id);

  try {
    const { data } = await apiClient.get<Car>(`/cars/${id}`);
    return data;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response?.status === 404) {
      throw new Error("Araç bulunamadı");
    }
    throw new Error(getApiErrorMessage(error, "CarService.fetchCarById"));
  }
};

export const fetchFeaturedCars = async (): Promise<Car[]> => {
  try {
    const { data } = await apiClient.get<Car[]>("/cars/featured");
    return data;
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "CarService.fetchFeaturedCars"));
  }
};
