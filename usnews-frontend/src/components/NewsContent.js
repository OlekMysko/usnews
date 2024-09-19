import React from 'react';

const NewsContent = ({ content }) => {
    return (
        <div
            style={{
                padding: '20px',
                backgroundColor: '#ffffff',
                border: '1px solid #ccc',
                borderRadius: '10px',
                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                maxWidth: '500px',
                textAlign: 'left',
                lineHeight: '1.6',
                fontSize: '16px',
            }}
        >
            <p>{content}</p>
        </div>
    );
};

export default NewsContent;
