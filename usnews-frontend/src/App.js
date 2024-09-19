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
                console.log('Fetched locations from aggregation:', response.data);
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
            console.log('Fetched aggregation data:', response.data);
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
        if (selectedNewsContent === content) {
            setSelectedNewsContent(null);
        } else {
            setSelectedNewsContent(content);
        }
    };

    return (
        <div>
            <h1 style={{ textAlign: 'center', marginTop: '20px' }}>US News</h1>
            <Map locations={locations} onLocationClick={handleLocationClick} />
            {selectedLocation && (
                <div style={{ textAlign: 'center', marginTop: '20px' }}>
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

                    {selectedNewsContent && <NewsContent content={selectedNewsContent} />}
                </div>
            )}
        </div>
    );
};

export default App;
