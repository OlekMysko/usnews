import React, { useState } from 'react';

const AddLocationForm = ({ onAddLocation }) => {
    const [newLocation, setNewLocation] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    const handleInputChange = (e) => {
        setNewLocation(e.target.value);
        if (error) setError('');
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        if (!newLocation) {
            setError('Location name is required.');
            return;
        }
        setIsLoading(true);
        await onAddLocation(newLocation);
        setIsLoading(false);
        setNewLocation('');
    };

    return (
        <form onSubmit={handleFormSubmit} style={{ marginBottom: '20px' }}>
            <input
                type="text"
                value={newLocation}
                onChange={handleInputChange}
                placeholder="Enter a city name"
                style={{ padding: '10px', marginRight: '10px', width: '300px' }}
            />
            <button type="submit" style={{ padding: '10px 20px' }} disabled={isLoading}>
                {isLoading ? 'Adding...' : 'Add Location'}
            </button>
            {error && <p style={{ color: 'red', marginTop: '10px' }}>{error}</p>}
        </form>
    );
};

export default AddLocationForm;
