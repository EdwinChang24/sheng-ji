build-web:
    ./gradlew mergeWeb
    cd build/web && pnpm i && pnpm astro build
build-web-ci:
    ./gradlew mergeWeb
    cd build/web && pnpm ci && pnpm astro build
