<script lang="ts">
    import ScrollAnim from "../../components/ScrollAnim.svelte";
    import Paragraph from "../components/Paragraph.svelte";
    import { easeInSine } from "../../util/easeIn";

    let height: number;
    let animState: number;

    const allRanks = ["2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"];
    const shownRanks = ["2", "3", "6", "7", "9", "10", "J", "A"];
    const animDuration = 8;
    $: radius = animState < 0 ? 100 : animState > 1 ? 250 : easeInSine(animState) * 150 + 100;

    $: opacity = animState < 0.25 ? 0 : animState > 0.75 ? 1 : animState * 2 - 0.5;
    $: scale = animState < 0 ? 0.25 : animState > 0.75 ? 1 : easeInSine(animState + 0.25);
</script>

<svelte:window bind:innerHeight={height} />

<div class="bg-slate-900">
    <ScrollAnim animHeight={height} opacityLimit={1.3} bind:animState>
        <div style:opacity>
            <div
                style:scale
                class="relative z-10 drop-shadow-[black_0_0_2em] flex justify-center mx-4"
            >
                <div class="text-6xl font-bold text-center">
                    <p class="inline">Play your</p>
                    <p
                        class="inline bg-clip-text text-transparent bg-gradient-to-tr from-red-700 to-white"
                    >
                        best hand.
                    </p>
                </div>
            </div>
            <svg
                class="absolute left-1/2 -translate-x-1/2 top-1/2 -translate-y-1/2 pointer-events-none"
                width="800px"
                height="800px"
                viewBox="0 0 800 800"
            >
                {#each shownRanks as rank}
                    <text
                        x="400"
                        y="400"
                        fill="white"
                        font-weight="bold"
                        font-size="3.5rem"
                        text-anchor="middle"
                        dominant-baseline="middle"
                    >
                        {rank}
                        <animateMotion
                            dur="{animDuration}s"
                            repeatCount="indefinite"
                            path="
                                M {radius} 0
                                A {radius} {radius} 0 1 1 -{radius} 0
                                A {radius} {radius} 0 1 1 {radius} 0
                            "
                            begin="{-(allRanks.indexOf(rank) / allRanks.length) * animDuration}s"
                        />
                    </text>
                {/each}
            </svg>
        </div>
    </ScrollAnim>
    <Paragraph>
        <p slot="header">Possible trumps</p>
        <div slot="body">
            <p class="inline">
                Your next card could be the trump card, and whether you call it might be
                the difference between winning and losing. You can't afford to forget which cards
                you can call.
            </p>
            <p class="inline text-nowrap">升级 Display</p>
            <p class="inline">
                shows you the current round's possible trumps so you won't miss a call.
            </p>
        </div>
    </Paragraph>
</div>
