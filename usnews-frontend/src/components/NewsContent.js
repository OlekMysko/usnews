import React from 'react';

const NewsContent = ({ content }) => {
    return (
        <div
            style={{
                marginTop: '20px',
                padding: '20px',
                backgroundColor: '#f0f0f0',
                border: '1px solid #ccc',
                borderRadius: '8px',
                maxWidth: '600px',
                margin: '20px auto',
                textAlign: 'left',
            }}
        >
            <p>{content}</p>
        </div>
    );
};

export default NewsContent;
