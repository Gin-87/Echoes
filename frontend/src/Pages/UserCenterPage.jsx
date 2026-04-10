import React from 'react';
import UserCenterLayout from '../Comp/components/UserCenter/Layout/UserCenterLayout';
import { changeLanguage } from '../utils/i18n/i18n.js';

function UserCenterPage() {
  return (
    <div style={{ width: '100%' }}>
      <UserCenterLayout />
    </div>
  );
}

export default UserCenterPage; 