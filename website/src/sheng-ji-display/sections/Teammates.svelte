<script lang="ts">
    import ScrollAnim from "../../components/ScrollAnim.svelte";
    import { easeInSine } from "../../util/easeIn";
    import Paragraph from "../components/Paragraph.svelte";

    let height: number;
    let animState: number;
    $: scale = animState < -1 ? 0 : animState > 1 ? 1 : easeInSine(animState / 2 + 0.5);
    $: opacity = animState < 0 ? 0 : animState > 0.5 ? 1 : animState * 2;

    const arrows = [40, 130, 160, 250, 330];
    const arrowLength = 600;
    $: arrowPath = `
        M0,50 h${animState * arrowLength}
        M${animState * arrowLength},50 L${animState * arrowLength - 5},55
        M${animState * arrowLength},50 L${animState * arrowLength - 5},45
        M${(animState * arrowLength) / 2},50 L${(animState * arrowLength) / 2 - 5},55
        M${(animState * arrowLength) / 2},50 L${(animState * arrowLength) / 2 - 5},45
    `;
</script>

<svelte:window bind:innerHeight={height} />

<div class="bg-slate-875">
    <ScrollAnim animHeight={height} opacityLimit={1.4} bind:animState>
        <div style:opacity>
            <div class="z-10 flex justify-center">
                <div class="text-5xl sm:text-6xl font-bold text-center drop-shadow-lg" style:scale>
                    <p class="inline">Don't forget your</p>
                    <p
                        class="inline bg-clip-text text-transparent bg-gradient-to-tr from-red-700 to-white"
                    >
                        friends.
                    </p>
                </div>
            </div>
            {#each arrows as arrow}
                <div class="absolute left-1/2 top-1/2 -translate-y-1/2 pointer-events-none">
                    <svg
                        viewBox="0 0 2000 100"
                        height="100px"
                        width="2000px"
                        style:transform="rotate({arrow}deg)"
                        class="arrow-mask origin-left"
                    >
                        <path
                            d={arrowPath}
                            stroke="#c1d4ff"
                            stroke-width="4px"
                            stroke-linecap="round"
                        />
                    </svg>
                </div>
            {/each}
        </div>
    </ScrollAnim>
    <Paragraph>
        <p slot="header">Teammates</p>
        <div slot="body">
            <p class="inline">
                When your hand isn't great, you need to work with your teammates to win the most
                crucial rounds.
            </p>
            <p class="inline text-nowrap">升级 Display</p>
            <p class="inline">
                shows arrows pointing to the house's team members so you always know who your
                friends are.
            </p>
        </div>
    </Paragraph>
</div>

<style lang="scss">
    .arrow-mask {
        mask-image: linear-gradient(to right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 1) 10%);
    }
</style>
