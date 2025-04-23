require('dotenv').config();

module.exports = {
    apps: [
        {
            name: 'mbn-back',
            script: 'java',
            args: [
                '-jar',
                'build/libs/mbn-0.0.1-SNAPSHOT.jar',
                '--server.port=3334'
            ],
            env: {
                HAEGI_SERVER_MBN_URL: process.env.HAEGI_SERVER_MBN_URL,
                HAEGI_SERVER_USERNAME: process.env.HAEGI_SERVER_USERNAME,
                HAEGI_SERVER_PASSWORD: process.env.HAEGI_SERVER_PASSWORD,
                JWT_SECRET: process.env.JWT_SECRET,
                KAKAO_CLIENT_ID: process.env.KAKAO_CLIENT_ID,
                KAKAO_REDIRECT_URL: process.env.KAKAO_REDIRECT_URL,
                FRONT_DOMAIN_URL: process.env.FRONT_DOMAIN_URL
            }
        }
    ]
}
