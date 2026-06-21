import { useAppSelector, useAppDispatch } from "../../app/hooks/storeHooks";
import type { RootState } from "../../store/store";
import { NavLink } from "react-router-dom";
import { useEffect } from "react";
import { AiOutlinePlus, AiOutlineUser } from "react-icons/ai";
import { fetchFeaturedCars } from "../../store/slices/carSlice";

import ReservationList from "./reservation/ReservationList";

const Dashboard = () => {
  const dispatch = useAppDispatch();
  const user = useAppSelector((state: RootState) => state.auth.user);

  useEffect(() => {
    dispatch(fetchFeaturedCars());
  }, [dispatch]);

  return (
    <div className="space-y-10 m-10 mt-20">
      {/* Header */}
      <div className="rounded-lg shadow-lg p-6 bg-white">
        <h1 className="text-2xl font-bold mb-2 text-gray-800">
          Merhaba, {user?.name || "Kullanıcı"} 👋
        </h1>
        <p className="text-gray-600">
          Rezervasyonlarını yönetebilir, yeni araç kiralayabilirsin.
        </p>
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 ">
        <NavLink
          to="/dashboard/cars"
          className="rounded-lg shadow-md p-4 hover:shadow-lg transition-all duration-300 border bg-white border-gray-300 hover:border-orange-500"
        >
          <div className="flex items-center space-x-4">
            <div className="w-12 h-12 rounded-lg flex items-center justify-center bg-orange-600">
              <AiOutlinePlus className="w-6 h-6 text-white" />
            </div>
            <div>
              <h3 className="text-lg font-semibold text-gray-800">
                Araç Kiralama
              </h3>
              <p className="text-sm text-gray-600">Mevcut araçları incele</p>
            </div>
          </div>
        </NavLink>

        <NavLink
          to="/dashboard/profile"
          className="rounded-lg shadow-md p-4 hover:shadow-lg transition-all duration-300 border bg-white border-gray-300 hover:border-orange-500"
        >
          <div className="flex items-center space-x-4">
            <div className="w-12 h-12 rounded-lg flex items-center justify-center bg-orange-600">
              <AiOutlineUser className="w-6 h-6 text-white" />
            </div>
            <div>
              <h3 className="text-lg font-semibold text-gray-800">Profilim</h3>
              <p className="text-sm text-gray-600">Hesap bilgilerini düzenle</p>
            </div>
          </div>
        </NavLink>
      </div>
      <ReservationList />
    </div>
  );
};

export default Dashboard;
