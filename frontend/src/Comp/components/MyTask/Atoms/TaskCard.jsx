import React, { useState } from 'react';
import { Card, CardContent, CardMedia, Typography, Box, Chip, IconButton, Tooltip, Switch } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useToast } from "../../General/Business/ToastContext.jsx";
import EditIcon from '@mui/icons-material/Edit';
import HistoryIcon from '@mui/icons-material/History';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';

const TaskCard = ({ task: initialTask }) => {
  const { t } = useTranslation();
  const { showToast } = useToast();
  const [task, setTask] = useState(initialTask);

  const getStatusColor = (status) => {
    return status === 'active'
      ? { color: '#4caf50', backgroundColor: '#e8f5e9' }
      : { color: '#9e9e9e', backgroundColor: '#f5f5f5' };
  };

  const handleStatusToggle = () => {
    const newStatus = task.status === 'active' ? 'inactive' : 'active';
    setTask({ ...task, status: newStatus });
    showToast(
      newStatus === 'active'
        ? t('task.activateSuccess')
        : t('task.deactivateSuccess'),
      'success'
    );
  };

  const handleEdit = () => {
    showToast(t('task.editNotImplemented'), 'info');
  };

  const handleHistory = () => {
    showToast(t('task.historyNotImplemented'), 'info');
  };

  const handleDelete = () => {
    showToast(t('task.deleteNotImplemented'), 'info');
  };

  return (
    <Card
      sx={{
        width: '100%',
        maxWidth: { xs: '100%', sm: '340px' },
        height: '500px',
        display: 'flex',
        flexDirection: 'column',
        borderRadius: '8px',
        overflow: 'hidden',
        '&:hover': {
          transform: 'translateY(-4px)',
          boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
          transition: 'all 0.3s ease-in-out'
        }
      }}
    >
      <Box
        sx={{
          position: 'relative',
          height: '325px',
          width: '100%',
          backgroundColor: '#f5f5f5',
        }}
      >
        <CardMedia
          component="img"
          sx={{
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            objectFit: 'cover',
          }}
          image={task.image}
          alt={task.characterName}
          loading="lazy"
        />
      </Box>

      <CardContent
        sx={{
          flex: 1,
          p: 2,
          pt: 1.5,
          backgroundColor: '#fff',
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
          <Typography
            variant="h6"
            component="div"
            sx={{
              fontSize: '1.1rem',
              fontWeight: 600,
              overflow: 'hidden',
              textOverflow: 'ellipsis',
              display: '-webkit-box',
              WebkitLineClamp: 1,
              WebkitBoxOrient: 'vertical',
              flex: 1,
              mr: 1
            }}
          >
            {task.taskName}
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <Chip
              label={t(task.status === 'active' ? 'task.active' : 'task.inactive')}
              size="small"
              sx={{
                height: 24,
                minWidth: 70,
                ...getStatusColor(task.status)
              }}
            />
            <Switch
              size="small"
              checked={task.status === 'active'}
              onChange={handleStatusToggle}
              sx={{
                '& .MuiSwitch-switchBase.Mui-checked': {
                  color: '#4caf50',
                  '&:hover': {
                    backgroundColor: '#4caf5014',
                  },
                },
                '& .MuiSwitch-switchBase.Mui-checked + .MuiSwitch-track': {
                  backgroundColor: '#4caf50',
                },
              }}
            />
          </Box>
        </Box>

        <Typography
          variant="body2"
          color="text.secondary"
          sx={{
            fontSize: '0.875rem',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap',
            mb: 1
          }}
        >
          {t('task.characterName')}: {task.characterName}
        </Typography>

        <Box
          sx={{
            mt: 'auto',
            display: 'flex',
            flexDirection: 'column',
            gap: 0.5
          }}
        >
          <Typography
            variant="body2"
            color="text.secondary"
            sx={{ fontSize: '0.75rem', whiteSpace: 'nowrap' }}
          >
            {t('task.createdAt')}: {task.createdAt}
          </Typography>
          <Typography
            variant="body2"
            color="text.secondary"
            sx={{ fontSize: '0.75rem', whiteSpace: 'nowrap' }}
          >
            {t('task.lastUpdated')}: {task.lastUpdated}
          </Typography>

          <Box
            sx={{
              display: 'flex',
              justifyContent: 'flex-end',
              gap: 1,
              mt: 1,
              borderTop: '1px solid #f0f0f0',
              pt: 1
            }}
          >
            <Tooltip title={t('task.edit')}>
              <IconButton size="small" onClick={handleEdit} sx={{ color: '#666' }}>
                <EditIcon fontSize="small" />
              </IconButton>
            </Tooltip>
            <Tooltip title={t('task.history')}>
              <IconButton size="small" onClick={handleHistory} sx={{ color: '#666' }}>
                <HistoryIcon fontSize="small" />
              </IconButton>
            </Tooltip>
            <Tooltip title={t('task.delete')}>
              <IconButton size="small" onClick={handleDelete} sx={{ color: '#666' }}>
                <DeleteOutlineIcon fontSize="small" />
              </IconButton>
            </Tooltip>
          </Box>
        </Box>
      </CardContent>
    </Card>
  );
};

export default TaskCard; 