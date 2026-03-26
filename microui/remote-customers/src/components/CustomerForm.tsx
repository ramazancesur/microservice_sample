import React, { useState } from 'react';
import { TextField, Stack } from '@mui/material';
import type { CreateCustomerPayload } from '../types/customer';

interface CustomerFormProps {
  initialValues?: Partial<CreateCustomerPayload>;
  onChange: (payload: CreateCustomerPayload) => void;
}

export function CustomerForm({ initialValues = {}, onChange }: CustomerFormProps) {
  const [values, setValues] = useState<CreateCustomerPayload>({
    firstName: initialValues.firstName ?? '',
    lastName: initialValues.lastName ?? '',
    email: initialValues.email ?? '',
    phone: initialValues.phone ?? '',
  });

  const handleChange = (field: keyof CreateCustomerPayload) => (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const updated = { ...values, [field]: event.target.value };
    setValues(updated);
    onChange(updated);
  };

  return (
    <Stack spacing={2} mt={1}>
      <TextField
        label="First Name"
        value={values.firstName}
        onChange={handleChange('firstName')}
        required
        fullWidth
      />
      <TextField
        label="Last Name"
        value={values.lastName}
        onChange={handleChange('lastName')}
        required
        fullWidth
      />
      <TextField
        label="Email"
        type="email"
        value={values.email}
        onChange={handleChange('email')}
        required
        fullWidth
      />
      <TextField
        label="Phone"
        value={values.phone}
        onChange={handleChange('phone')}
        fullWidth
      />
    </Stack>
  );
}
