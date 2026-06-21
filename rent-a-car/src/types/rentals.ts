export type Rental = {
  id?: number;
  userId?: number;
  carId?: number;
  startDate: string;
  endDate: string;
  totalPrice: number;
  status: string;
  city: string;
  location: string;
  createdAt?: string;
  updatedAt?: string;
  cars?: {
    make: string;
    model: string;
    year: number;
  };
};

export type CreateRentalRequest = {
  carId: number;
  startDate: string;
  endDate: string;
  totalPrice: number;
  location: string;
};
