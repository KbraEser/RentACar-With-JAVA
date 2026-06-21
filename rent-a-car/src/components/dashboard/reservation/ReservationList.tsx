import { useEffect } from "react";
import { useAppSelector, useAppDispatch } from "../../../app/hooks/storeHooks";
import type { RootState } from "../../../store/store";
import {
  cancelReservation,
  fetchRentals,
} from "../../../store/slices/rentalsSlice";
import ReservationListError from "./ReservationListError";
import ReservationListNull from "./ReservationListNull";
import { toast } from "react-toastify";

const ReservationList = () => {
  const dispatch = useAppDispatch();
  const user = useAppSelector((state: RootState) => state.auth.user);
  const { rentals, loading, error } = useAppSelector(
    (state: RootState) => state.rentals
  );

  useEffect(() => {
    if (user) {
      dispatch(fetchRentals());
    }
  }, [dispatch, user]);

  if (error) {
    return <ReservationListError />;
  }
  if (rentals.length === 0) {
    return <ReservationListNull />;
  }

  const handleCancelClick = (reservationId: string) => {
    toast(({ closeToast }) => (
      <div style={{ textAlign: "center" }}>
        <p>Rezervasyonu iptal etmek istediğinizden emin misiniz?</p>
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            gap: "10px",
            marginTop: "10px",
          }}
        >
          <button
            onClick={() => {
              dispatch(cancelReservation(reservationId));
              toast.success("Rezervasyon başarıyla iptal edildi");
              closeToast();
            }}
            style={{
              backgroundColor: "var(--color-orange-600)",
              color: "white",
              border: "none",
              padding: "8px 16px",
              borderRadius: "4px",
              cursor: "pointer",
            }}
          >
            Evet, İptal Et
          </button>
        </div>
      </div>
    ));
  };

  return (
    <div className="space-y-6 mt-10 ">
      {" "}
      <div
        className="rounded-lg shadow-lg p-6 "
        style={{ backgroundColor: "var(--color-white)" }}
      >
        {" "}
        <h1
          className="text-xl font-bold mb-4"
          style={{ color: "var(--color-gray-800)" }}
        >
          Rezervasyon Listesi
        </h1>
        <div className="overflow-x-auto ">
          <table className="w-full border-collapse rounded-lg shadow-lg">
            <thead>
              <tr>
                <th className="text-left p-2 text-gray-600">Araç</th>
                <th className="text-left p-2 text-gray-600">Başlangıç</th>
                <th className="text-left p-2 text-gray-600">Bitiş </th>
                <th className="text-left p-2 text-gray-600">Toplam Fiyat</th>

                <th className="text-left p-2 text-gray-600">Alış Yeri</th>
              </tr>
            </thead>
            <tbody>
              {rentals.map((reservation) => {
                return (
                  <tr
                    className="border-b"
                    key={reservation.id || reservation.carId}
                  >
                    <td className=" p-2">
                      {reservation.cars
                        ? `${reservation.cars.make} ${reservation.cars.model}`
                        : `Araç ID: ${reservation.carId}`}
                    </td>
                    <td className=" p-2">
                      {new Date(reservation.startDate).toLocaleDateString(
                        "tr-TR"
                      )}
                    </td>
                    <td className=" p-2">
                      {new Date(reservation.endDate).toLocaleDateString(
                        "tr-TR"
                      )}
                    </td>
                    <td className=" p-2 justify-center ">
                      {" "}
                      {reservation.totalPrice} ₺
                    </td>

                    <td className=" p-2">
                      {reservation.city}-{reservation.location}
                    </td>
                    <td className=" p-2">
                      <button
                        onClick={() =>
                          handleCancelClick(String(reservation.id ?? ""))
                        }
                        disabled={reservation.status === "cancelled"}
                        className={`primary-button ${
                          reservation.status === "cancelled"
                            ? "opacity-75 cursor-not-allowed"
                            : ""
                        }`}
                      >
                        {reservation.status === "active"
                          ? "İptal Et"
                          : "İptal Edildi"}
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
        <div className="w-full"></div>
      </div>
    </div>
  );
};

export default ReservationList;
