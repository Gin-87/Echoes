export const checkMenuPermission = async (resource) => {
  try {
    const response = await fetch('/api/permissions/menu', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });
    const menuIds = await response.json();
    return menuIds.includes(resource);
  } catch (error) {
    console.error('Failed to check menu permission:', error);
    return false;
  }
}; 