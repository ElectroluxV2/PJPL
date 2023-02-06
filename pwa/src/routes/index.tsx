import { component$ } from '@builder.io/qwik';
import type { DocumentHead } from '@builder.io/qwik-city';
import '@material/web/button/outlined-button.js';

export default component$(() => {
  const r = new Array(100).fill(0).map((v, i) => i);

  return (
    <>
        <md-outlined-button label="Back"></md-outlined-button>

        <h1>test</h1>
      <ul>
        {r.map((i) => (
            <li key={i}>Item - {i}</li>
        ))}
      </ul>
      <a href="/test">Test</a>
    </>
  );
});

export const head: DocumentHead = {
  title: 'Welcome to Qwik',
  meta: [
    {
      name: 'description',
      content: 'Qwik site description',
    },
  ],
};
