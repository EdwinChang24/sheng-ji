<script lang="ts">
    import ScrollAnim from "../../components/ScrollAnim.svelte";
    import { easeInSine } from "../../util/easeIn";
    import trumpDisplayPreview from "../../assets/trump-display-preview.svg";
    import callsDisplayPreview from "../../assets/calls-display-preview.svg";
    import Paragraph from "../components/Paragraph.svelte";

    let height: number;
    let animState: number;
    $: opacity = animState < -0.5 ? 0 : animState > 0 ? 1 : animState * 2 + 1;
    $: scale = animState < -0.5 ? 0 : animState > 1 ? 1 : animState / 3 + 2 / 3;
    $: rotation = animState > 1 ? 0 : easeInSine(1 - animState) * 90;
</script>

<svelte:window bind:innerHeight={height} />

<div class="bg-slate-875">
    <ScrollAnim animHeight={height} opacityLimit={2} bind:animState>
        <div
            class="flex flex-col justify-center text-center"
            style:opacity
            style:scale
            style:transform="rotate({rotation}deg)"
        >
            <div class="rotate-180 mb-16 mx-8 flex justify-center scale-75">
                <img src={trumpDisplayPreview.src} alt="Trump Display" class="h-48 w-auto" />
            </div>
            <div class="text-5xl -my-8 sm:my-0 sm:text-6xl font-bold">
                <p class="inline">Know the</p>
                <p
                    class="inline bg-clip-text text-transparent bg-gradient-to-tr from-red-700 to-white"
                >
                    essentials.
                </p>
            </div>
            <div class="mt-24 mx-8 flex justify-center">
                <img src={callsDisplayPreview.src} alt="Calls Display" class="h-48 w-auto" />
            </div>
        </div>
    </ScrollAnim>

    <Paragraph>
        <p slot="header">Trump and calls</p>
        <div slot="body">
            <p class="inline">
                Never lose track again of which of your cards are trump, or join the losing team
                without realizing.
            </p>
            <p class="inline text-nowrap">升级 Display</p>
            <p class="inline">shows you what you need to know to win.</p>
        </div>
    </Paragraph>
</div>
