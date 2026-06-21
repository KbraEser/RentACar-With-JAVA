import type { CreateRentalRequest, Rental } from "../types/rentals";
import { apiClient } from "../lib/apiClient";
import { getApiErrorMessage } from "../utils/errorHandler";

export const createReservationService = async (request: CreateRentalRequest) => {
  try {
    const { data } = await apiClient.post<Rental>("/rentals", request);
    return data;
  } catch (error) {
    throw new Error(
      getApiErrorMessage(error, "ReservationService.createReservationService")
    );
  }
};

export const fetchRentalsService = async () => {
  try {
    const { data } = await apiClient.get<Rental[]>("/rentals");
    return data;
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "ReservationService.fetchRentalsService"));
  }
};

export const fetchRentalsByStatusDate = async (carId: string) => {
  try {
    const { data } = await apiClient.get<Rental[]>(`/rentals/car/${carId}/active`);
    return data;
  } catch (error) {
    throw new Error(
      getApiErrorMessage(error, "ReservationService.fetchRentalsByStatusDate")
    );
  }
};

export const cancelReservationService = async (id: string) => {
  try {
    const { data } = await apiClient.put<Rental>(`/rentals/${id}/cancel`);
    return data;
  } catch (error) {
    throw new Error(
      getApiErrorMessage(error, "ReservationService.cancelReservationService")
    );
  }
};
