build-web:
    ./gradlew mergeWeb
    cd build/web && pnpm i && pnpm astro build
