import { Navigate } from 'react-router-dom';
import HomePage from '../Pages/HomePage';
import MyTaskPage from '../pages/MyTaskPage';
import UserCenterPage from '../Pages/UserCenterPage';
import CreateCharacterPage from '../Pages/CreateCharacterPage';
import CharacterDetail from '../Comp/components/Admin/Sections/CharacterDetail';
import MyModelPage from '../Pages/MyModelPage';
import CharacterManagement from '../Comp/components/Admin/Sections/CharacterManagement';
import UserManagement from '../Comp/components/Admin/Sections/UserManagement';
import AdminPage from '../pages/AdminPage';
import CreateLetter from '../Comp/components/Letter/CreateLetter';
import { isPublicRoute } from '../utils/routeUtils';

const routes = [
  {
    path: '/',
    element: <Navigate to="/home" replace />
  },
  {
    path: '/home',
    element: <HomePage />,
    public: true
  },
  {
    path: '/admin',
    element: <AdminPage />,
    requiresAuth: true,
    children: [
      {
        path: '', // 空路径表示默认子路由
        element: <Navigate to="characters" replace />
      },
      {
        path: 'characters',
        element: <CharacterManagement />
      },
      {
        path: 'users',
        element: <UserManagement />
      }
    ]
  },
  {
    path: '/my-tasks',
    element: <MyTaskPage />
  },
  {
    path: '/user-center',
    element: <UserCenterPage />
  },
  {
    path: '/user-center/points',
    element: <UserCenterPage />
  },
  {
    path: '/create-character',
    element: <CreateCharacterPage />,
  },
  {
    path: '/character/:id',
    element: <CharacterDetail />,
  },
  {
    path: '/my-models',
    element: <MyModelPage />
  },
  {
    path: '/letter/create/:id',
    element: <CreateLetter />
  },
  {
    path: '/write/:id',
    element: <CreateLetter />
  }
];

export default routes; 