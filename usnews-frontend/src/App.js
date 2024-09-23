import React, {useState, useEffect} from 'react';
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
        setSelectedNewsContent(content);
    };

    const handleBackClick = () => {
        setSelectedNewsContent(null);
    };

    return (
        <div>
            <h1 style={{textAlign: 'center', marginTop: '20px'}}>US News</h1>
            <Map locations={locations} onLocationClick={handleLocationClick}/>
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
