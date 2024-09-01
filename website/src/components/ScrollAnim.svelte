<script lang="ts">
    export let animHeight: number;
    export let opacityLimit = 1.5;
    export let animState: number;
    let innerHeight: number | undefined;
    let elem: HTMLDivElement | undefined;
    let scrollY = 0;
    let elemHeight = 0;
    $: {
        scrollY;
        if (elem === undefined || innerHeight === undefined) {
            animState = 0;
        } else {
            const rect = elem.getBoundingClientRect();
            animState = (innerHeight / 2 - rect.top) / animHeight;
        }
    }

    $: opacity =
        animState < 1
            ? 1
            : animState > opacityLimit
              ? 0
              : (opacityLimit - animState) / (opacityLimit - 1);
</script>

<svelte:window bind:innerHeight bind:scrollY />

<div style:padding="{elemHeight / 2}px 0 0 0">
    <div
        bind:this={elem}
        class="flex justify-center items-baseline"
        style:height="{animHeight + elemHeight}px"
    >
        <div
            bind:offsetHeight={elemHeight}
            class="{(animState ?? 0) < 0.5
                ? 'sticky'
                : 'fixed'} top-[50dvh] -translate-y-1/2 w-full"
            style="
            filter: opacity({opacity});
            visibility: {(animState ?? 0) < opacityLimit ? 'visible' : 'hidden'};
        "
        >
            <slot {animState} />
        </div>
    </div>
</div>
