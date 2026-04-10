import React from 'react';
import { Box } from '@mui/material';
import CreateCharacter from '../Comp/components/Admin/Sections/CreateCharacter';

const CreateCharacterPage = () => {
  return (
    <Box
      sx={{
        minHeight: '100vh',
        backgroundColor: '#fff',
      }}
    >
      <CreateCharacter />
    </Box>
  );
};

export default CreateCharacterPage; 