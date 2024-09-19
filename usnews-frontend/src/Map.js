import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';

const defaultIcon = L.icon({
    iconUrl: markerIcon,
    shadowUrl: markerShadow,
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41],
});

const Map = ({ locations, onLocationClick }) => {
    console.log('Locations in Map component:', locations);

    return (
        <MapContainer center={[37.8, -96]} zoom={4} style={{ height: '600px', width: '100%' }}>
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            />
            {locations.map((location) => (
                <Marker
                    key={location.locationId}
                    position={[location.locationLatitude, location.locationLongitude]}
                    icon={defaultIcon}
                    eventHandlers={{
                        click: () => {
                            onLocationClick(location);
                        },
                    }}
                >
                    <Popup>
                        {location.locationName}
                    </Popup>
                </Marker>
            ))}
        </MapContainer>
    );
};

export default Map;
