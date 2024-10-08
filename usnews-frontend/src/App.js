import React, {useState, useEffect} from 'react';
import Map from './Map';
import axios from 'axios';
import NewsList from './components/NewsList';
import NewsContent from './components/NewsContent';
import AddLocationForm from './components/AddLocationForm';
import {
    LOCATION_FAILED,
    LOCATION_SUCCESS,
    LOCATION_NOT_FOUND,
    AGGREGATION_URL,
    LOCATION_ALREADY_EXISTS, LOCATION, LOCATIONS, LOCATION_BY_COORDINATES
} from './components/constants';

const App = () => {
    const [locations, setLocations] = useState([]);
    const [selectedLocation, setSelectedLocation] = useState(null);
    const [news, setNews] = useState([]);
    const [selectedNewsContent, setSelectedNewsContent] = useState(null);
    const [addLocationMessage, setAddLocationMessage] = useState('');
    const [mapRefreshKey, setMapRefreshKey] = useState(0);

    const fetchNewsForUserLocation = async (latitude, longitude) => {
        try {
            const response = await axios.get(`${AGGREGATION_URL}${LOCATION_BY_COORDINATES}`, {
                params: {lat: latitude, lon: longitude}
            });
            setSelectedLocation(response.data.data.location);
            setNews(response.data.data.newsList);
        } catch (error) {
            console.error('Error fetching news for user location:', error);
            setNews([]);
        }
    };

    useEffect(() => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const latitude = position.coords.latitude;
                    const longitude = position.coords.longitude;
                    fetchNewsForUserLocation(latitude, longitude);
                },
                (error) => {
                    console.error('Error fetching geolocation:', error);
                }
            );
        }
    }, []);

    useEffect(() => {
        const fetchLocationsFromAggregation = async () => {
            try {
                const response = await axios.get(`${AGGREGATION_URL}${LOCATIONS}`);
                setLocations(response.data.data);
            } catch (error) {
                console.error('Error fetching locations:', error);
            }
        };
        fetchLocationsFromAggregation();
    }, []);

    const fetchNewsForLocation = async (locationId) => {
        setNews([]);
        setSelectedNewsContent(null);
        try {
            const response = await axios.get(`${AGGREGATION_URL}${LOCATION}${locationId}`);
            setNews(response.data.data.newsList);
        } catch (error) {
            console.error('Error fetching news:', error);
            setNews([]);
        }
    };

    const handleLocationClick = (location) => {
        setSelectedLocation(location);
        fetchNewsForLocation(location.locationId);
    };

    const handleNewsClick = (content) => {
        setSelectedNewsContent(content);
    };

    const handleBackClick = () => {
        setSelectedNewsContent(null);
    };

    const handleError = (error, newLocation) => {
        if (error.response?.status === 400) {
            const detailsMessage = error.response?.data?.details?.message || LOCATION_ALREADY_EXISTS;
            setAddLocationMessage(`${LOCATION_FAILED} ${detailsMessage}`);
        } else if (error.response?.status === 404) {
            setAddLocationMessage(`${LOCATION_NOT_FOUND} ${newLocation}`);
        } else {
            const errorMessage = error.response?.data?.message || error.message;
            setAddLocationMessage(`${LOCATION_FAILED} ${errorMessage}`);
        }
    };

    const waitForLocation = async (locationId, retries = 5, delay = 1000) => {
        for (let i = 0; i < retries; i++) {
            try {
                const response = await axios.get(`${AGGREGATION_URL}${LOCATION}${locationId}`);
                if (response.data.success) {
                    return response.data.data;
                }
            } catch (error) {
                console.error(`Attempt ${i + 1} failed: ${error.message}`);
            }
            await new Promise(res => setTimeout(res, delay));
        }
        throw new Error('Failed to retrieve location after several attempts');
    };

    const handleAddLocation = async (newLocation) => {
        try {
            const response = await axios.post(`${AGGREGATION_URL}/${newLocation}`);
            if (response.data.success) {
                const newLocationData = response.data.data;
                setAddLocationMessage(`${LOCATION_SUCCESS} ${newLocation}`);
                setLocations([...locations, newLocationData]);

                const confirmedLocation = await waitForLocation(newLocationData.locationId);

                setSelectedLocation(confirmedLocation);
                await fetchNewsForLocation(newLocationData.locationId);

                setMapRefreshKey(prevKey => prevKey + 1);
            } else {
                setAddLocationMessage(`${LOCATION_FAILED} ${response.data.details.message}`);
            }
        } catch (error) {
            handleError(error, newLocation);
        }
    };

    return (
        <div>
            <h1 style={{textAlign: 'center', marginTop: '20px'}}>US News</h1>
            <Map key={mapRefreshKey} locations={locations} onLocationClick={handleLocationClick}/>
            <div style={{textAlign: 'center', marginTop: '20px'}}>
                <AddLocationForm onAddLocation={handleAddLocation}/>
                {addLocationMessage && <p>{addLocationMessage}</p>}
            </div>
            {selectedLocation && (
                <div className="container" style={{marginTop: '20px', textAlign: 'center'}}>
                    <h2>News for {selectedLocation.locationName}</h2>
                    {!selectedNewsContent ? (
                        <>
                            {news && news.length > 0 ? (
                                <NewsList
                                    news={news}
                                    onNewsClick={handleNewsClick}
                                />
                            ) : (
                                <p>No news available for this location.</p>
                            )}
                        </>
                    ) : (
                        <div>
                            <button onClick={handleBackClick} style={{marginBottom: '20px'}}>Back to news list</button>
                            <NewsContent content={selectedNewsContent}/>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default App;
