import './App.css'
import { useState } from 'react'


export function TwitterFollowCard ( { userName, children, initialIsFollowing}) {

    
    const imageSrc = userName ? `https://unavatar.io/x/${userName}` : undefined
    
    const [isFollowing, setIsFollowing] = useState(initialIsFollowing)
    const text = isFollowing ? 'Siguiendo' : 'Seguir'
    const btnClassName = isFollowing ? 'tw-followCard-button is-following' : 'tw-followCard-button'


    const handleClick = () => {
        setIsFollowing(!isFollowing)
    }

    return (
        <article className='tw-followCard'>
            <header className='tw-followCard-header'>
                {imageSrc && (
                    <img
                        className='tw-followCard-image'
                        alt={`Avatar de ${userName}`}
                        src={imageSrc}
                    />
                )}
                <div className='tw-followCard-info'>
                    <strong>{children}</strong>
                    <span className='tw-followCard-infoUserName'>@{userName}</span>
                </div>
            </header>

            <aside>
                <button className={btnClassName} onClick={handleClick}>
                    <span className='tw-followCard-text'>{text}</span>
                    <span className='tw-followCard-stopFollow'>Dejar de seguir</span>
                </button>
            </aside>
        </article>
    )
}