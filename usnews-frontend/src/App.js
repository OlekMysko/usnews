import React, { useState, useEffect } from 'react';
import Map from './Map';
import axios from 'axios';
import NewsList from './components/NewsList';
import NewsContent from './components/NewsContent';

const App = () => {
    const [locations, setLocations] = useState([]);
    const [selectedLocation, setSelectedLocation] = useState(null);
    const [news, setNews] = useState([]);
    const [selectedNewsContent, setSelectedNewsContent] = useState(null);

    useEffect(() => {
        const fetchLocationsFromAggregation = async () => {
            try {
                const response = await axios.get('/aggregation/locations');
                setLocations(response.data.data);
            } catch (error) {
                console.error('Error fetching locations:', error);
            }
        };
        fetchLocationsFromAggregation();
    }, []);

    const fetchNewsForLocation = async (locationId) => {
        try {
            const response = await axios.get(`/aggregation/location/${locationId}`);
            setNews(response.data.data.newsList);
        } catch (error) {
            console.error('Error fetching news:', error);
        }
    };

    const handleLocationClick = (location) => {
        setSelectedLocation(location);
        fetchNewsForLocation(location.locationId);
        setSelectedNewsContent(null);
    };

    const handleNewsClick = (content) => {
        setSelectedNewsContent(content === selectedNewsContent ? null : content);
    };

    return (
        <div style={{ margin: '20px' }}>
            <h1 style={{ textAlign: 'center' }}>US News</h1>
            <Map locations={locations} onLocationClick={handleLocationClick} />
            {selectedLocation && (
                <div style={{ display: 'flex', marginTop: '20px' }}>
                    <div style={{ flex: 1 }}>
                        <h2>News for {selectedLocation.locationName}</h2>
                        {news && news.length > 0 ? (
                            <NewsList
                                news={news}
                                onNewsClick={handleNewsClick}
                                selectedNewsContent={selectedNewsContent}
                            />
                        ) : (
                            <p>No news available for this location.</p>
                        )}
                    </div>
                    {selectedNewsContent && (
                        <div style={{ flex: 1, marginLeft: '20px' }}>
                            <NewsContent content={selectedNewsContent} />
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default App;
