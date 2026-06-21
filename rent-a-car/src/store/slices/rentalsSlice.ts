import type { CreateRentalRequest, Rental } from "../../types/rentals";
import {
  cancelReservationService,
  createReservationService,
  fetchRentalsService,
  fetchRentalsByStatusDate as fetchRentalsByStatusDateService,
} from "../../services/reservationService";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ERROR_MESSAGES } from "../../types/errors";

type RentalsState = {
  rentals: Rental[];
  carReservations: Rental[];
  loading: boolean;
  error: string | null;
};

const initialState: RentalsState = {
  rentals: [],
  carReservations: [],
  loading: false,
  error: null,
};

export const createReservation = createAsyncThunk(
  "rentals/createReservation",
  async (request: CreateRentalRequest) => {
    return await createReservationService(request);
  }
);

export const fetchRentals = createAsyncThunk("rentals/fetchRentals", async () => {
  return await fetchRentalsService();
});

export const fetchRentalsByStatusDate = createAsyncThunk(
  "rentals/fetchRentalsByStatusDate",
  async (carId: string) => {
    return await fetchRentalsByStatusDateService(carId);
  }
);

export const cancelReservation = createAsyncThunk(
  "rentals/cancelReservation",
  async (id: string) => {
    return await cancelReservationService(id);
  }
);

const rentalsSlice = createSlice({
  name: "rentals",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(createReservation.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createReservation.fulfilled, (state, action) => {
        state.loading = false;
        state.rentals.push(action.payload);
      })
      .addCase(createReservation.rejected, (state, action) => {
        state.loading = false;
        state.error =
          action.error.message || ERROR_MESSAGES.RESERVATION_CREATE_FAILED;
      })
      .addCase(fetchRentals.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchRentals.fulfilled, (state, action) => {
        state.loading = false;
        state.rentals = action.payload;
      })
      .addCase(fetchRentals.rejected, (state, action) => {
        state.loading = false;
        state.error =
          action.error.message || ERROR_MESSAGES.RESERVATION_FETCH_FAILED;
      })
      .addCase(cancelReservation.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(cancelReservation.fulfilled, (state, action) => {
        state.loading = false;
        const cancelledId = String(action.meta.arg);
        const rentalIndex = state.rentals.findIndex(
          (rental) => String(rental.id) === cancelledId
        );

        if (rentalIndex !== -1) {
          state.rentals[rentalIndex].status = "cancelled";
        }
      })
      .addCase(cancelReservation.rejected, (state, action) => {
        state.loading = false;
        state.error =
          action.error.message || ERROR_MESSAGES.RESERVATION_CANCEL_FAILED;
      })
      .addCase(fetchRentalsByStatusDate.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchRentalsByStatusDate.fulfilled, (state, action) => {
        state.loading = false;
        state.carReservations = action.payload;
      })
      .addCase(fetchRentalsByStatusDate.rejected, (state, action) => {
        state.loading = false;
        state.error =
          action.error.message || ERROR_MESSAGES.RESERVATION_FETCH_FAILED;
      });
  },
});

export default rentalsSlice.reducer;
