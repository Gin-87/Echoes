import React from 'react';
import {
  Box,
  MenuItem,
  Button,
  FormControl,
  Select,
  InputBase,
  styled,
  Typography,
} from '@mui/material';
import { useTranslation } from 'react-i18next';

// 自定义输入框样式
const StyledInput = styled(InputBase)(({ theme }) => ({
  '& .MuiInputBase-input': {
    borderRadius: 4,
    position: 'relative',
    backgroundColor: theme.palette.background.paper,
    border: '1px solid #ced4da',
    fontSize: 14,
    padding: '8px 12px',
    transition: theme.transitions.create(['border-color', 'box-shadow']),
    '&:hover': {
      borderColor: theme.palette.grey[400],
    },
    '&:focus': {
      borderColor: theme.palette.primary.main,
    },
  },
}));

const FilterBar = ({ filters, onFilterChange, onSearch, onReset }) => {
  const { t } = useTranslation();

  const handleChange = (field, value) => {
    onFilterChange(field, value);
  };

  return (
    <Box
      sx={{
        mb: 3,
        display: 'flex',
        flexWrap: 'wrap',
        gap: 3,
        alignItems: 'flex-start',
      }}
    >
      {filters.map((filter) => (
        <Box key={filter.field} sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
          <Typography 
            variant="body2" 
            sx={{ 
              color: 'text.secondary',
              fontSize: '14px',
            }}
          >
            {filter.label}
          </Typography>
          {filter.type === 'select' ? (
            <FormControl
              sx={{ minWidth: 180 }}
              size="small"
            >
              <Select
                value={filter.value || ''}
                onChange={(e) => handleChange(filter.field, e.target.value)}
                input={<StyledInput />}
                displayEmpty
                renderValue={(selected) => {
                  if (!selected) {
                    return t('admin.all');
                  }
                  const selectedOption = filter.options.find(opt => opt.value === selected);
                  return selectedOption ? selectedOption.label : selected;
                }}
                notched={undefined}
              >
                {filter.options.map((option) => (
                  <MenuItem key={option.value} value={option.value}>
                    {option.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          ) : (
            <StyledInput
              placeholder={t('admin.pleaseEnter')}
              value={filter.value || ''}
              onChange={(e) => handleChange(filter.field, e.target.value)}
              sx={{ minWidth: 180 }}
            />
          )}
        </Box>
      ))}

      {/* 搜索 & 重置 按钮 */}
      <Box sx={{ display: 'flex', gap: 1, alignSelf: 'flex-end' }}>
        <Button
          variant="contained"
          onClick={onSearch}
          sx={{
            minWidth: '80px',
            boxShadow: 'none',
            '&:hover': {
              boxShadow: 'none',
            },
          }}
        >
          {t('searchButtonPlaceHolder')}
        </Button>
        <Button
          variant="outlined"
          onClick={onReset}
          sx={{ minWidth: '80px' }}
        >
          {t('admin.reset')}
        </Button>
      </Box>
    </Box>
  );
};

export default FilterBar;
