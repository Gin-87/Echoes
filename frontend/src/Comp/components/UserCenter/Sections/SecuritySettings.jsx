import React from 'react';
import { Box, Button, TextField } from '@mui/material';
import { useTranslation } from 'react-i18next';

function SecuritySettings() {
  const { t } = useTranslation();

  return (
    <Box component="form" sx={{ maxWidth: 400 }}>
      <TextField
        fullWidth
        type="password"
        label={t('userCenter.currentPassword')}
        sx={{ mb: 2 }}
      />
      <TextField
        fullWidth
        type="password"
        label={t('userCenter.newPassword')}
        sx={{ mb: 2 }}
      />
      <TextField
        fullWidth
        type="password"
        label={t('userCenter.confirmPassword')}
        sx={{ mb: 3 }}
      />
      <Button variant="contained">
        {t('userCenter.updatePassword')}
      </Button>
    </Box>
  );
}

export default SecuritySettings; 