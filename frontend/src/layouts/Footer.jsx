import React from 'react';

function Footer() {
    return (
        <div>
            <div className="footer">
                <div className="website-name">
                    Market
                </div>
                <div className="options">
                    <a href="/"> Homepage </a>
                    <a href="/products"> Shop </a>
                    <a href="#">Support </a>
                    <a href="#">Contact us </a>
                    <a href="#">About us </a>
                </div>
                <div className="links">
                    <div className="link-icon">
                        <a href="https://www.linkedin.com/in/moataz-tahri-35528a256/" target="_blank">
                            <i className="fa-brands fa-linkedin"/>
                        </a>
                    </div>
                    <div className="link-icon">
                        <i className="fa-brands fa-facebook"/>
                    </div>
                    <div className="link-icon">
                        <i className="fa-brands fa-instagram"/>
                    </div>
                    <div className="link-icon">
                        <i className="fa-brands fa-youtube"></i>
                    </div>
                </div>
                <div className="copyrights">
                    <i className="fa fa-copyright mx-2"/>
                    All rights reserved. 2025
                </div>
            </div>
        </div>
    );
}

export default Footer;